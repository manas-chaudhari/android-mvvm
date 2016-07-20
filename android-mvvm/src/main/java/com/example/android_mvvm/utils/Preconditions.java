package com.example.android_mvvm.utils;

class Preconditions {
    static <T> void checkNotNull(T value, String name) {
        if (value == null) {
            throw new NullPointerException(name + " should not be null");
        }
    }
}
