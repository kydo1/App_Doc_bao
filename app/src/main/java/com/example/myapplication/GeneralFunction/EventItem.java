package com.example.myapplication.GeneralFunction;

public interface EventItem<T> {
    void onDeleteItem(T object);
    void onClickItem(T object);
}
