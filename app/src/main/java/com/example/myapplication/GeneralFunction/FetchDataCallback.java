package com.example.myapplication.GeneralFunction;

import java.util.ArrayList;

public interface FetchDataCallback<T> {
    void onSuccess(ArrayList<T> result);
    void onError(String message);
}
