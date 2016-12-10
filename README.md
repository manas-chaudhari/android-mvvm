# Android MVVM
[![Build Status](https://travis-ci.org/manas-chaudhari/android-mvvm.svg?branch=master)](https://travis-ci.org/manas-chaudhari/android-mvvm) [ ![Download](https://api.bintray.com/packages/manas-chaudhari/maven/android-mvvm/images/download.svg) ](https://bintray.com/manas-chaudhari/maven/android-mvvm/_latestVersion)

Contents:

1. Sample project to demonstrate a coding pattern based on MVVM with focus on
  1. Easy composition of views
  1. Minimising boilerplate of setting up views
  1. Reuse of presentation logic
1. A library with essential tools for the pattern

## Prerequisites

1. [Android Data Binding](https://developer.android.com/topic/libraries/data-binding/index.html)
1. [RxJava](https://github.com/ReactiveX/RxJava)
  - [2-minute intro to Rx](https://medium.com/@andrestaltz/2-minute-introduction-to-rx-24c8ca793877#.dh92eypp8)
  - [Grokking RxJava](http://blog.danlew.net/2014/09/15/grokking-rxjava-part-1/)
  - [Intro to Reactive Programming by André Staltz](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754)
  - [Comprehensive list](http://reactivex.io/tutorials.html)
1. [Introduction to this pattern @DroidconIN](https://www.youtube.com/watch?v=JaB8SXCSbgg&t=1s)

## Quick Tutorial

[Getting Started](Documentation/GettingStarted.md) provides a tutorial to setup the library and gives an idea about its functionality. As the main deliverable of this library is the pattern, it is important to understand the principles behind it, so that the pattern can be applied even at places where the library APIs aren't available.

## MVVM Implementation

This pattern makes use of Data Binding, such that views contain exactly 1 variable `vm` i.e. ViewModel. Idea is that the ViewModel should have all information required to display the View. Multiple views can share a single view model. This helps in reusing functionality for a different layout.

Using a single variable `vm` provides a consistent mechanism to configure any View:
```
viewBinding.setVariable(BR.vm, viewModel)
```

Note that this mechanism needs to be configured by providing an instance of `ViewModelBinder` interface. This interface describes how a ViewModel is bound to a View. The variable name to be used in xmls (`vm` in this example) should be specified here.

```java
BindingUtils.setDefaultBinder(new ViewModelBinder() {
    @Override
    public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
        viewDataBinding.setVariable(BR.vm, viewModel);
    }
});
```
This code can be placed in your `onCreate` method of Application/Activity.


## Creating a View Model
A view model is a POJO formed by fields for storing data (for input/output) and event listeners. In MVVM, interaction between ViewModel and View happens via changes in data OR method calls. These interactions can be categorized based on direction of data flow:

1. **Output Data (ViewModel -> View)** ViewModel changes its data, View listens for changes & updates itself
    Example: ViewModel is running a timer, and updating its `timeText` variable. View is listening for changes and updating itself whenever it gets a callback.

1. **Input Data (View -> ViewModel)** View requests ViewModel to update its data (such as capturing text input)
    Example: Form containing various inputs like `text` for `EditText`, `isChecked` for `Checkbox`.

1. **Input Events (View -> ViewModel)** View invokes functions on ViewModel when certain events occur (such as onSwipe/onClick/onFocused)
    Note the difference between `Data` and `Event`. Any input can be represented as an `Event`. However, wherever there is persistence in input, representing them as `Data` is convenient.
    For example, if a user has typed `Googl` in EditText, this input is represented as a String variable with value `Googl`. When user types `e`, the text changed "event" causes the data to change to `Google`. Working with data is better in this example.
    Consider an example of a Button click. There is no data change that's implicitly happening on click. Hence, this input is represented as an `Event`.

Now, we'll see how these three interactions are implemented in this pattern.

### Output Data
This is stored as a field in the ViewModel class. If the value is constant, it can be simply declared as a `final TYPE`. If its changing, it is declared as an `ObservableField<TYPE>`, provided by data binding. Example for displaying text (String)

```java
public class OutputDataViewModel {
  public final String constantOutput = "";
  public final ObservableField<String> changingOutput = new ObservableField<String>("");
}
```
> Note that everything is `final`. To modify the `changingOutput`, its `set` function is used. Example: `this.changingOutput.set("new value");`. Data binding takes care of adding listeners and updating the view.

In XML, this data is displayed by referring to these fields.

```xml
<TextView ...
  android:text="@{vm.constantOutput}" />

<TextView ...
  android:text="@{vm.changingOutput}" />
```


### Input Data
As input data implies that it cannot be constant, it is always stored as an `ObservableField<TYPE>`.

```java
public class InputDataViewModel {
  public final ObservableField<String> inputText = new ObservableField<String>("");
}
```

Because we want the view to update the value of this `ObservableField`, [Two Way Binding](https://medium.com/google-developers/android-data-binding-lets-flip-this-thing-dc17792d6c24#.eee8cuo08) is used.
```xml
<EditText ...
  android:text="@={vm.inputText}" />
```
> The `@=` enables two way binding


### Input Events
EventListeners can be implemented simply as methods in ViewModel
```java
public class EventViewModel {
  MessageHelper messageHelper; // This is an external dependency

  public void onClick() {
    messageHelper.show("Something got clicked");
  }  
}
```
```xml
<Button
  android:onClick="@{(v) -> vm.onClick()}"/>
```

#### Event Listeners using `Runnable`
Another approach is to implement listeners as `Runnable` fields inside ViewModel.

```java
public class EventViewModel {
  MessageHelper messageHelper; // This is an external dependency

  public final Runnable onClick = () -> {
    messageHelper.show("Something got clicked");    
  };
}
```

```xml
<Button
  android:onClick="@{vm.onClick}"/>
```

For this to work, a `BindingConversion` between `View.OnClickListener` and `Runnable` needs to be defined.
```java
@BindingConversion
public static View.OnClickListener toOnClickListener(final Runnable runnable) {
    if (runnable != null) {
        return () -> runnable.call();
    } else {
        return null;
    }
}
```
This static function can be placed anywhere in your code.

#### Event Listeners using `rx.PublishSubject`

Using a `BindingAdapter`/`BindingConversion`, click event can be bound through a `PublishSubject`.
```java
class ItemViewModel {
  public final PublishSubject<ItemViewModel> onSelect;
}

// When working with a list of view models, its easy to merge events.
Observable<Item> onAnyItemSelect = itemViewModels.map { vm -> vm.onSubmit }.merge().map { vm -> vm.item };
```

> There are various ways to define Event handlers. There are no compelling points to stick to a fixed way. Use the approach which suits you the best

## ViewModel is unaware about the View

This is the core principle behind MVVM. Naturally, ViewModel cannot have a reference to `View` or any subclass. In addition to these, ViewModel cannot know about any platform-specific implementation details. Thus, Android classes such as `Activity`, `Fragment`, `Context` cannot be referenced.

> This also means that the ViewModel logic is same across different platforms such as iOS or web

Any platform-dependent functionality is abstracted into an `interface` and then provided to ViewModel. In the above example, `MessageHelper` is an `interface` for displaying a message to user. The activity can choose to implement it using `Toast` or any other mechanism. Keeping these implementations outside allows sharing them. For example, `MessageHelper` can be implemented in a `BaseActivity`, which could be a base class for all activities.

In a deep hierarchy, fulfilling such dependencies can result in a lot of boilerplate. In that case, it is recommended to use dependency injection libraries to keep things clean. A minimal example using [Dagger2](http://google.github.io/dagger/) is available on [extras/dagger](https://github.com/manas-chaudhari/android-mvvm/tree/extras/dagger) branch.

### Testability

As ViewModels do not reference Android classes, testing them is straight forward. Tests are written as plain Unit Tests. As all dependencies are interfaces, they get easily mocked.

```java
@Test
public void detailsPage_isOpened_onClick() throws Exception {
    Item item = new Item("Item 1", null);
    Navigator mockNavigator = mock(Navigator.class);
    ItemViewModel viewModel = new ItemViewModel(item, mockNavigator);

    viewModel.itemClicked.call();

    verify(mockNavigator).openDetailsPage(item);
}
```
> This example uses [Mockito](https://github.com/mockito/mockito) framework for mocking


## Using RxJava

RxJava provides a great bunch of operators for handling changes. For example, if a dynamic field of a model needs to be formatted before displaying, its convenient to store it as an `rx.Observable<TYPE>`. The `map` operator can be used to format the values.

`FieldUtils` class provides methods for converting between RxJava's `Observable` and Data Binding's `ObservableField` types. This allows the use of RxJava's operators to manipulate the data.

### Rx -> DataBinding

Example: `Cart` has a `getTotalAmount()` method that returns an `Observable<Float>`. Amount needs to be formatted before displaying. This can be implemented as follows:

```java
public final ReadOnlyField<String> totalAmountText;
public CartViewModel(Cart cart) {
  totalAmountText = FieldUtils.toField(cart.getTotalAmount().map(a -> a + " Rs"));
}
```

The `toField` method returns an instance of `ReadOnlyField` which extends `ObservableField`. Note that `set` method in `ReadOnlyField` does nothing. See [Observables And Setters](Documentation/ObservablesAndSetters.md) for the rationale behind this.


### DataBinding -> Rx

Example: Error needs to be shown if input text is empty.

```java
static import FieldUtils.toObservable;
static import FieldUtils.toField;

public final ObservableField<String> inputText = new ObservableField<>("");
public final ReadOnlyField<Boolean> errorVisible = toField(toObservable(inputText).map(text -> text.isEmpty()));
```

```xml
<EditText
  android:text="@={vm.inputText}"/>
<ErrorView
  android:visibility="@{vm.errorVisible}"/>
```

A binding adapter would be required to use boolean for `visibility` attribute.
```java
@BindingAdapter("android:visibility")
public static void bindVisibility(@NonNull View view, @Nullable Boolean visible) {
    int visibility = (visible != null && visible) ? View.VISIBLE : View.GONE;
    view.setVisibility(visibility);
}
```

See [SearchViewModel.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/two_way_binding/SearchViewModel.java) and the corresponding [activity_search.xml](sample/src/main/res/layout/activity_search.xml) for another example in which the search results get updated as user updates the query.


## Composing ViewModels

> TODO:
> For generic adapters in XML, keep data binding as the primary way.
> Examples page


Lets say a page requires to combine 3 functionalities. There can be 1 ViewModel to represent each functionality. Similar to how layout hierarchy is created using `<include>`, a parent ViewModel can be created per combination containing child ViewModels as properties. Data Binding allows binding included layout's variables.

```xml
<ParentLayout>
  <include layout="@layout/child_view"
    bind:vm="@{vm.childVm}" />
</ParentLayout/>
```

### Composing a dynamic list of functionalities


## Setting up complex views

Having a common setup mechanism allows writing abstract adapters, which can be reused for displaying any type of views. This reduces a lot of boilerplate. For example, a RecyclerView can be setup with these two inputs:
- `Observable<List<ViewModel>>`: A list of ViewModels. The adapter notifies itself when the list updates
- `ViewProvider`: An interface which decides which View should be used for a ViewModel

Using Data Binding, we can create attributes so that these inputs can be provided in XML:

```xml
<android.support.v7.widget.RecyclerView
    bind:items="@{vm.itemVms}"
    bind:view_provider="@{@layout/row_item_without_image}" />
```

This provides a declarative API to setup views like RecyclerView/ViewPager. This idea can be applied to any widget which requires any custom setup. The setup code gets moved to one or more BindingAdapters, and the data inputs are provided in XML.



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

> It is often required to reuse same mapping of ViewModel -> View. One way to provide these is using a static class like [ViewProviders.java](sample/src/main/java/com/manaschaudhari/android_mvvm/sample/ViewProviders.java)


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

## More Information
[Wiki](https://github.com/manas-chaudhari/android-mvvm/wiki) contains links to more content around this topic.
