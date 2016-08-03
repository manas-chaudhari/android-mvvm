# Android MVVM [![Build Status](https://travis-ci.org/manas-chaudhari/android-mvvm.svg?branch=master)](https://travis-ci.org/manas-chaudhari/android-mvvm)

Contents:

1. Sample project to demonstrate a coding pattern based on MVVM with focus on
  1. Easy composition of views
  1. Minimising boilerplate of setting up views
  1. Reuse of presentation logic
1. A library with essential tools for the pattern

## MVVM Implementation
This pattern makes use of Data Binding, such that views contain exactly 1 variable `vm` i.e. ViewModel. Idea is that the ViewModel should have all information required to display the View. Multiple views can share a single view model. This helps in reusing functionality for a different layout.

Using a single variable `vm` provides a consistent mechanism to configure any View:
```
viewBinding.setVariable(BR.vm, viewModel)
```

Having a common setup mechanism allows writing abstract adapters, which can be reused for displaying any types of views. This reduces a lot of boiler plate. For example, displaying items in a recycler view should require only two inputs:
- `Observable<List<ViewModel>>`: Observable List of ViewModels. The adapter notifies itself when the list updates
- `ViewProvider`: An interface which decides which View should be used for a ViewModel

This library does not make any assumption about the binding mechanism. Hence, a third argument is required:
- `ViewModelBinder`: An interface which decides how a ViewModel should be bound to a View

> It is recommended to use a constant instance of `ViewModelBinder` so that this argument isn't required to be passed at all places. See [BindingAdapters.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/BindingAdapters.java) for example.

> It is often required to reuse same mapping of ViewModel -> View. One way to provide these is using a static class like [ViewProviders.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/ViewProviders.java)
> See [Composing ViewModels](#composing-viewmodels) section for examples

## Creating a View Model
A view model is formed by fields for storing data (for input/output) and event listeners.

### One Way binding: ReadOnlyField
This field is meant for only displaying data. Hence the name 'one way' (ViewModel -> View). As changing values can be easily managed using `rx.Observable`, this library provides a class `ReadOnlyField` which can be created from `rx.Observable`.
> `set` method in `ReadOnlyField` does nothing. See [Observables And Setters](Documentation/ObservablesAndSetters.md).

```java
public final ReadOnlyField<String> totalAmountString;
public ViewModel(Cart cart) {
  totalAmountString = ReadOnlyField.create(cart.totalAmount.map(q -> q + " Rs"))
}
```


### Two Way binding
In order to capture inputs of user, plain `ObservableField` can be used with Two Way Binding syntax. An `ObservableField` can be converted to an `Observable` using `BindingUtils` provided.
```java
static import BindingUtils.*

public final ObservableField<String> inputText = new ObservableField<>("");
public final ReadOnlyField<Boolean> errorVisible = ReadOnlyField.create(toObservable(inputText).map(text -> text.isEmpty()))
```

```xml
<EditText
  android:text="@={vm.inputText}"/>
<ErrorView
  android:visible="@{vm.errorVisible}"/>
```
> A binding adapter would be required to use boolean for visibility.
> `@=` syntax hasn't been documented officially. See: https://halfthought.wordpress.com/2016/03/23/2-way-data-binding-on-android/

See [SearchViewModel.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/two_way_binding/SearchViewModel.java) and the corresponding [activity_search.xml](sample/src/main/res/layout/activity_search.xml) for an example. The value in ObservableField of `EditText` updates when user types and the text displayed updates if value of ObservableField is modified.

### Binding Events
EventListeners can be implemented simply as methods in ViewModel
```java
MessageHelper messageHelper; // This is an external dependency

public void onClick(View v) { // View argument is not meant to be used
  messageHelper.show("Something got clicked");
}
```
```xml
<Button
  android:onClick="@{vm.onClick}"/>
```

It is important to keep the ViewModel unaware about concrete implementations of platform dependent functionalities (Showing a message in this case). The activity can choose to `Show a message` using `Toast` or any other mechanism. Keeping these implementations outside allows sharing them. For example, `MessageHelper` can be implemented in a BaseActivity.

In a deep hierarchy, fulfilling dependencies can result in a lot of boilerplate.
In that case, it is recommended to use dependency injection libraries to keep things clean.
Complete dependency injection is outside the scope of this project. A minimal example using [Dagger2](http://google.github.io/dagger/) is available on [extras/dagger](https://github.com/manas-chaudhari/android-mvvm/tree/extras/dagger) branch.

> This is merely for demo and not an ideal implementation.

#### Removing View argument

As having a `View` instance inside ViewModel violates MVVM principles, its cleaner to write custom BindingAdapter to allow event handlers without arguments. Another approach is to write handlers as instances of `Action0` with a `BindingConversion` to `OnClickListener`
See [ItemViewModel.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/ItemViewModel.java) for examples

If it is not required to perform any action on click of a view inside the ViewModel, it could be useful to expose click event through a `PublishSubject`.
```java
class ItemViewModel {
  public final PublishSubject<ItemViewModel> onSelect;
}

// When working with a list of view models, its easy to merge events.
Observable<Item> onAnyItemSelect = itemViewModels.map { vm -> vm.onSubmit }.merge().map { vm -> vm.item }
```

> There are various ways to define Event handlers. There are no compelling points to stick to a fixed way. Use the approach which suits you the best

## Composing ViewModels
Lets say a page requires to combine 3 functionalities. There can be 1 ViewModel to represent each functionality. Similar to how layout hierarchy is created using `<include>`, a parent ViewModel can be created per combination containing child ViewModels as properties. Data Binding allows binding included layout's variables.

```xml
<ParentLayout>
  <include layout="@layout/child_view"
    bind:vm="@{vm.childVm}" />
</ParentLayout/>
```

### Composing a dynamic list of functionalities
Android provides several widgets for displaying a dynamic list of views, for eg: `RecyclerView`, `ViewPager`. All adapters can be constructed from item list, ViewProvider and ViewModelBinder. Android may or may not expose view attached events to adapters, hence lifecycle of some adapters needs to be managed.

| Adapters provided in this library | Lifecycle |
| --- | --- |
|[RecyclerViewAdapter](android-mvvm/src/main/java/com/manaschaudhari/android_mvvm/adapters/RecyclerViewAdapter.java) | Auto |
|[ViewPagerAdapter](android-mvvm/src/main/java/com/manaschaudhari/android_mvvm/adapters/ViewPagerAdapter.java) | Manual |

> Do raise an issue to request for more

#### Manual lifecycle
These adapters require extra setup and cleanup because Android does not provide events when attaching/removing from their view. These adapters implement `Connectable` interface. Whenever an adapter is set, it is required that `connect()` method should be invoked. When adapter is reset, the `Subscription` returned by `connect` should be unsubscribed.

To prevent additional boiler plate code, a [BindingUtils.java](android-mvvm/src/main/java/com/manaschaudhari/android_mvvm/utils/BindingUtils.java) provides wrappers for binding adapters which also take care of connecting and unsubscribing adapters.

#### Using different views
```java
Observable<List<ViewModel>> items = // Perhaps get these from an API OR database
RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, new ViewProvider() {
          @Override
          public int getView(ViewModel vm) {
            if (vm instanceof ItemViewModel) {
              return ((ItemViewModel) vm).hasImage() ? R.layout.row_item_with_image : R.layout.row_item_without_image;
            } else if (vm instanceof SomeOtherViewModel) {
              return R.layout.some_other_view;
            }
            return 0;
          }
        }, defaultBinder);
```

## Data Binding to reduce boilerplate
See yourself creating RecyclerViewAdapter in every Activity? Well, there is no need to. By using custom `BindingAdapter`s, one can remove all the code from your activities, and provide minimal arguments from XML.
For example, one can write these binding adapters for a recycler view:

```xml
<import type="ViewProviders" />

<!--Example with dynamic views-->
<android.support.v7.widget.RecyclerView
    bind:items="@{vm.itemVms}"
    bind:layout_vertical="@{true}"
    bind:view_provider="@{ViewProviders.itemListing}" />

<!--Example With Static Views-->
<android.support.v7.widget.RecyclerView
    bind:items="@{vm.itemVms}"
    bind:layout_vertical="@{true}"
    bind:view_provider="@{@layout/row_item_without_image}" />

<!--Same arguments for ViewPager-->
<android.support.v4.widget.ViewPager
    bind:items="@{vm.itemVms}"
    bind:view_provider="@{ViewProviders.itemListing}" />

```

### Adapters provided in library
BindingAdapters to work with `RecyclerViewAdapter` and `ViewPagerAdapter` have been provided with the library in [BindingUtils.java](android-mvvm/src/main/java/com/manaschaudhari/android_mvvm/utils/BindingUtils.java).
The above examples will work out of the box provided you have set the defaultBinder. For example:
```java
BindingUtils.setDefaultBinder(new ViewModelBinder() {
    @Override
    public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
        viewDataBinding.setVariable(com.manaschaudhari.android_mvvm.sample.BR.vm, viewModel);
    }
});
```
This library will provide BindingAdapters related to the components it provides.
Hence, it is important to write your own adapters to reduce other boilerplate code.

> Although BindingAdapters can be overriden, it hasn't been specified how databinding resolves the conflicts. Based on experiments, adapters in client project are preferred over adapters from library. However, having identical adapters in a same module will result in undeterministic results.

> The sample project overrides these BindingAdapters to check memory leaks

## Preventing Memory Leaks

Guidelines to prevent memory leaks:

- Use `BindingUtils` for binding adapters
- Make sure ViewModel is set to `null` when Activity is destroyed

  ```java
  binding.setVm(null);
  binding.executePendingBindings();
  ```
- Never subscribe to any field inside a ViewModel. Derive the action based on some other observable
- Stay as [Functional](#functional-viewmodels) as possible

The sample project uses LeakCanary to ensure that there are no leaks. This is only for demonstration purposes as the adapters have been tested against leaks. However, they provide a good example for testing leaks in binding adapters. See [BindingAdapters.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/BindingAdapters.java)


## Reuse Scenarios

Here are some scenarios, and the way in which this pattern resolves them:

### Multiple layouts for displaying same information
A common view model that can bind to all views.

### Two layouts, which share some common functionality
There are many ways depending on the situation.
- Two view models, one extending the other
- Two view models which extend from a common base
- Single view model with all functionality
  - This approach is possible only because ViewModel has no dependency to a view. In architectures like MVP, this is difficult, as the Presenter has a dependency on a View


## Functional ViewModels

RxJava provides several operators to compose Observables. Conversion between `rx.Observable` and `ViewModel` fields, enables the use of all these operators in ViewModels, which eliminates the need for mutable state (in most cases).

Consider an example of showing ProgressBar while api is loading. Traditional example:
```java
void load() {
  displayProgress();
  service.loadData(new Callback<String>() {
    @Override
    void onSuccess(String data) {
      displayData(data);
    }

    @Override
    void onError() {
      displayError();
    }
  });
}
```

By keeping loadedData as an `Observable`, we can derive progressVisibility by making use of the [Using](http://reactivex.io/documentation/operators/using.html) operator. From `progressVisibility` and `loadedData`, `errorVisibility` can be derived. Thus, there are no mutable states, only mapping from one Observable to other. Also, note that there is no need for subscriptions inside ViewModel as View will subscribe to the data after binding.

See [DataLoadingViewModel.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/functional/DataLoadingViewModel.java) for this example.
