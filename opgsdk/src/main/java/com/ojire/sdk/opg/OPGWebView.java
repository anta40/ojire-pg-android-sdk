package com.ojire.sdk.opg;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OPGWebView extends WebView  {

    private String foo;

    public void setFoo(String newFoo){
        this.foo = newFoo;
    }

    public String getFoo(){
        return this.foo;
    }

    public OPGWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Retrieve the attributes defined in attrs.xml
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OPGWebView,
                0, 0);

        try {
            foo = a.getString(R.styleable.OPGWebView_foo);
        } finally {
            // Always recycle the TypedArray to avoid memory leaks
            a.recycle();
        }
    }
}
