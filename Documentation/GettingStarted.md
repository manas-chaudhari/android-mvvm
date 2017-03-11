# Getting Started

This is a quick tutorial to setup this library and display a list of items in a RecyclerView using its tools.

### Enable Data Binding

In app's build.gradle, enable dataBinding under `android` section
```
android {
  ...

  dataBinding {
    enabled = true
  }
}
```

### Include Gradle Dependency

`compile 'com.manaschaudhari.android-mvvm:android-mvvm:0.2.0'`


### Initialize Library

Create a custom Application class and update the name in `AndroidManifest.xml`.

```java
public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BindingUtils.setDefaultBinder(new ViewModelBinder() {
            @Override
            public void bind(ViewDataBinding viewDataBinding, ViewModel viewModel) {
                viewDataBinding.setVariable(BR.vm, viewModel);
            }
        });
    }
}
```
Note that this won't compile until you create a layout with a data binding variable called `vm`. You can use a different name according to your convenience, however it has to be consistent across all layouts.

# Example

### Create ItemViewModel

```java
import com.manaschaudhari.android_mvvm.ViewModel;

class ItemViewModel implements ViewModel {
  final String name;

  public ItemViewModel(String name) { this.name = name }
}
```

### Create Item Layout XML

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="vm"
            type="com.manaschaudhari.android_mvvm.sample.ItemViewModel" />
    </data>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@{vm.name}" />
</layout>
```

### Create ItemListingViewModel for our page

```java
import com.manaschaudhari.android_mvvm.ViewModel;

public class ItemListViewModel implements ViewModel {
    public final Observable<List<ViewModel>> items;

    public ItemListingViewModel() {
        items = Observable.just("Item 1", "Item 2", "Item 3")
                .map(new Func1<String, ViewModel>() {
                    @Override
                    public ViewModel call(String s) {
                        return new ItemViewModel(s);
                    }
                }).toList();
    }
}
```

### Create/update an Activity

```java
public class ItemListActivity extends MvvmActivity {

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ItemListViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_item_list;
    }
}
```
`MvvmActivity` takes care of setting up the view and binding the ViewModel.


And now the xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/apk/res-auto">

    <data>

        <variable
            name="vm"
            type="com.example.quickstart.ItemListViewModel" />
    </data>

    <android.support.v7.widget.RecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        bind:items="@{vm.items}"
        bind:layout_vertical="@{true}"
        bind:view_provider="@{@layout/layout_item}"
        />
</layout>
```

Take a moment to appreciate the minimal setup for a RecyclerView.

That's it. Run the app, and you should see three items.
