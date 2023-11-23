package com.example.myapplication.GeneralFunction;

import android.net.Uri;

import java.util.ArrayList;

public interface UploadImageInterface {
    void onSuccess(Uri uri);
    void onError(String err);
}
