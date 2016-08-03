# Why ReadOnlyField

You might think why one needs to make a field ReadOnly in order to use it with `rx.Observable`.

Short answer: In order to maintain consistency. By disabling setter, the value in field is always equal to the one emitted by the `rx.Observable`. Disabling setter forces you to work through the Observable and thus, keeps imperative code away.

Another problem in allowing setter is that it will result in unpredictable behavior.
Consider a simple source `Observable.just(1)`. Now, when the view binds, our field will subscribe to the source, and thus set `1` to its value. Now, lets say we set the value to `2`. When the view unbinds, all subscriptions would get cleaned up and when it will rebind, it will subscribe again and hence, the value will change back to `1`. As binding state is unavailable to the ViewModel, in its perspective, _there's no guarantee that the value set will be retained and even if it is, it can get lost any time_.

Also, making VM aware about Binding State implies knowing about the view, which goes against the MVVM principle.

> Hence, setter in ReadOnlyField has been marked as `@Deprecated` and even if invoked, it does nothing

# Setter is required in TwoWay Binding

Although, setter need not be invoked from view model, it is used by the data binding library for two way binding. Hence, making setter work with `rx.Observable` would help in writing cleaner Two Way binding fields.

When the source is/behaves like a `PublishSubject`, these problems don't appear. Because subscribing to such a source will only emit future events, the field can be easily configured to use the latest value between source & setter. However, `Publish` like sources are suitable only for some usecases for eg. UI events like click.

When the source emits previously cached values, the information about latest value gets lost. Hence, it is not possible to decide whether to use the value emitted by the source observable OR the one set by the setter.

> Possible approach: Attach timestamp with each value, so that even if it gets cached, the timestamp contains information. More work required here
