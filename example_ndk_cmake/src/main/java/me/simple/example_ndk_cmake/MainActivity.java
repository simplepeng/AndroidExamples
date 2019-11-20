package me.simple.example_ndk_cmake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "example_ndk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void callJniMethod(View view) {
        String message = callJniMethod();
        Log.d(TAG, "callJniMethod --> " + message);
    }

    public void jniCallJavaMethod(View view) {
        jniCallJavaMethod();
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static {
        System.loadLibrary("hello_jni");
    }

    public static native String callJniMethod();

    public static native void jniCallJavaMethod();
}
