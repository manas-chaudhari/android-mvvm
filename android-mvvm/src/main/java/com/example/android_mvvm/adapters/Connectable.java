package com.example.android_mvvm.adapters;

import rx.Subscription;

public interface Connectable {
    Subscription connect();
}
