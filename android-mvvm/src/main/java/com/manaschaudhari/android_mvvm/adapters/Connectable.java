package com.manaschaudhari.android_mvvm.adapters;

import rx.Subscription;

public interface Connectable {
    Subscription connect();
}
