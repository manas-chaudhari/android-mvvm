
Android provides several widgets for displaying a dynamic list of views, for eg: `RecyclerView`, `ViewPager`. All adapters can be constructed from item list, ViewProvider and ViewModelBinder. Working with `Observable<List<>>` requires subscribing and cleanup. Widgets may or may not expose view attached events to adapters, hence lifecycle of some adapters needs to be managed.

| Adapters provided in this library | Lifecycle |
| --- | --- |
|[RecyclerViewAdapter](../android-mvvm/src/main/java/com/manaschaudhari/android_mvvm/adapters/RecyclerViewAdapter.java) | Auto |
|[ViewPagerAdapter](../android-mvvm/src/main/java/com/manaschaudhari/android_mvvm/adapters/ViewPagerAdapter.java) | Manual |

> Do raise an issue to request for more

#### Manual lifecycle
These adapters require extra setup and cleanup because Android does not provide events when attaching/removing from their view. These adapters implement `Connectable` interface. Whenever an adapter is set, it is required that `connect()` method should be invoked. When adapter is reset, the `Subscription` returned by `connect` should be unsubscribed.

To prevent additional boiler plate code, a [BindingUtils.java](../android-mvvm/src/main/java/com/manaschaudhari/android_mvvm/utils/BindingUtils.java) provides wrappers for binding adapters which also take care of connecting and unsubscribing adapters.
