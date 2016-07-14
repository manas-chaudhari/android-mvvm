# Android MVVM [![Build Status](https://travis-ci.org/manas-chaudhari/android-mvvm.svg?branch=master)](https://travis-ci.org/manas-chaudhari/android-mvvm)

Objectives:
1. To showcase how MVVM can be implemented on Android and to provide tools for doing so
1. To showcase how reusability of presentation logic using MVVM architecture


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

> It is recommended to use a constant instance of `ViewModelBinder` so that this argument isn't required to be passed at all places. See [BindingAdapter.java](https://github.com/manas-chaudhari/android-mvvm/blob/master/sample/src/main/java/com/example/android_mvvm/sample/BindingAdapters.java) for example.

> It is often required to reuse same mapping of ViewModel -> View. One way to provide these is using a static class like [ViewProviders.java](https://github.com/manas-chaudhari/android-mvvm/blob/master/sample/src/main/java/com/example/android_mvvm/sample/ViewProviders.java)


## Reusability

Here are some scenarios, and the way in which this pattern resolves them:

### Multiple layouts for displaying same information
A common view model that can bind to all views.

### Two layouts, which share some common functionality
There are many ways depending on the situation.
- Two view models, one extending the other
- Two view models which extend from a common base
- Single view model with all functionality
  - This approach is possible only because ViewModel has no dependency to a view. In architectures like MVP, this is difficult, as the Presenter has a dependency on a View


### Composing different combinations of functionalities
There can be 1 ViewModel to represent each functionality. Similar to how layout hierarchy is created using `<include>`, a parent ViewModel can be created per combination containing child ViewModels as properties. Data Binding allows binding included layout's variables.

```xml
<ParentLayout>
  <include layout="@layout/child_view"
    bind:vm="@{vm.childVm}" />
</ParentLayout/>
```

## TODO
- [ ] Add link to sample code for each reuse scenario
- [ ] Using Rx. One Way Binding, Two Way Binding
- [ ] Functional ViewModels
