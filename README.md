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

> It is recommended to use a constant instance of `ViewModelBinder` so that this argument isn't required to be passed at all places. See [BindingAdapters.java](https://github.com/manas-chaudhari/android-mvvm/blob/master/sample/src/main/java/com/example/android_mvvm/sample/BindingAdapters.java) for example.

> It is often required to reuse same mapping of ViewModel -> View. One way to provide these is using a static class like [ViewProviders.java](https://github.com/manas-chaudhari/android-mvvm/blob/master/sample/src/main/java/com/example/android_mvvm/sample/ViewProviders.java)
> See [Composing ViewModels](#composing-viewmodels) section for examples

## Creating a View Model
A view model is formed by fields for storing data (for input/output) and event listeners.

### One Way binding: ReadOnlyField
This field is meant for only displaying data. Hence the name 'one way' (ViewModel -> View). As changing values can be easily managed using `rx.Observable`, this library provides a class `ReadOnlyField` which can be created from `rx.Observable`.
> Although `set` method is available in `ReadOnlyField`, calling it can result in indeterminate behavior
> Calling `set` indicates an imperative style which should be avoided when using RxJava

```java
public final ReadOnlyField<String> totalAmountString;
public ViewModel(Cart cart) {
  totalAmountString = new ReadOnlyField(cart.totalAmount.map(q -> q + " Rs"))
}
```


### Two Way binding
In order to capture inputs of user, plain `ObservableField` can be used with Two Way Binding syntax. An `ObservableField` can be converted to an `Observable` using `BindingUtils` provided.
```java
static import BindingUtils.*

public final ObservableField<String> inputText = new ObservableField<>("");
public final ReadOnlyField<Boolean> errorVisible = new ReadOnlyField(toObservable(inputText).map(text -> text.isEmpty()))
```

```xml
<TextView
  android:text="@={vm.inputText}"/>
<ErrorView
  android:visible="@{vm.errorVisible}"/>
```
> A binding adapter would be required to use boolean for visibility.
> `@=` syntax hasn't been documented officially. See: https://halfthought.wordpress.com/2016/03/23/2-way-data-binding-on-android/

### Binding Events
EventListeners are methods in ViewModel
```
public void onSubmit() {
  // Do your thing
}
```
> A binding adapter would be required to remove the View argument

If there is no intention to perform any computation on click of a view, it could be useful to expose click event through a `PublishSubject`.
```java
class ItemViewModel {
  public final PublishSubject<ItemViewModel> onSelect;
}

// When working with a list of view models, its easy to merge events.
Observable<Item> onAnyItemSelect = itemViewModels.map { vm -> vm.onSubmit }.merge().map { vm -> vm.item }
```

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
|[RecyclerViewAdapter](https://github.com/manas-chaudhari/android-mvvm/blob/master/android-mvvm/src/main/java/com/example/android_mvvm/adapters/RecyclerViewAdapter.java) | Auto |
|[ViewPagerAdapter](https://github.com/manas-chaudhari/android-mvvm/blob/master/android-mvvm/src/main/java/com/example/android_mvvm/adapters/ViewPagerAdapter.java) | Manual |

> Do raise an issue to request for more

#### Manual lifecycle
These adapters require extra setup and cleanup because Android does not provide events when attaching/removing from their view. These adapters implement `Connectable` interface. Whenever an adapter is set, it is required that `connect()` method should be invoked. When adapter is reset, the `Subscription` returned by `connect` should be unsubscribed.

To prevent additional boiler plate code, a [BindingUtils.java](https://github.com/manas-chaudhari/android-mvvm/blob/master/android-mvvm/src/main/java/com/example/android_mvvm/utils/BindingUtils.java) provides wrappers for binding adapters which also take care of connecting and unsubscribing adapters.

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
This example has been implemented in sample. See [BindingAdapters.java](https://github.com/manas-chaudhari/android-mvvm/blob/master/sample/src/main/java/com/example/android_mvvm/sample/BindingAdapters.java)


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

---

## TODO
- [ ] Functional ViewModels
