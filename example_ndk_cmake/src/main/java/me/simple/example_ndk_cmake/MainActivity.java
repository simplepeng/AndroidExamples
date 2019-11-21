package me.simple.example_ndk_cmake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "example_ndk";

    public String filedA = "hello filed A";
    public static int filedStaticB = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void callJniMethod(View view) {
        String message = callJniMethod();
//        Log.d(TAG, "callJniMethod --> " + message);
        toast(message);
    }

    public void jniCallJavaMethod(View view) {
        Test test = new Test();
        jniCallJavaMethod();
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static void staticLog(String text) {
        Log.d(TAG, "staticLog text == " + text);
    }

    static {
        System.loadLibrary("hello_jni");
    }

    public void jniCallJavaStaticMethod(View view) {
        native_jniCallJavaStaticMethod();
    }

    public void jniCallJavaFiled(View view){
        Test test = new Test();
        native_callJavaFiled(test);
    }

    public void jniCallJavaStaticFiled(View view){
        native_callJavaStaticFiled();
    }

    public void jniCallJavaConstruct(View view){
        native_jniCallJavaConstruct();
    }

    public void throwException(View view){
        native_throwException();
    }

    public void jniCheckException(View view){
        native_jniCheckException();
    }

    /**
     * 调用c的方法
     */
    public static native String callJniMethod();

    /**
     * JNI获取Java类的变量
     */
    public static native void native_callJavaFiled(Test test);

    /**
     * JNI获取Java类的static变量
     */
    public static native void native_callJavaStaticFiled();

    /**
     * jni调用java的方法
     */
    public static native void jniCallJavaMethod();

    /**
     * jni调用java的static方法
     */
    public static native void native_jniCallJavaStaticMethod();

    /**
     * jni调用java的有参构造方法
     */
    public static native void native_jniCallJavaConstruct();

    public static native void native_throwException();
    public static native void native_jniCheckException();

}
