package me.simple.example_ndk_cmake;

import android.util.Log;

public class Test {

    private int argA;
    private String argB;

    public Test(int a, String b) {
        this.argA = a;
        this.argB = b;

        Log.d(MainActivity.TAG, "argA == " + argA);
        Log.d(MainActivity.TAG, "argB == " + argB);
    }

    public Test() {
    }

    private int a = 1;
    public static String b = "hello";

    public static void staticLog(String message) {
        Log.d(MainActivity.TAG, message);
    }

    public void log(String message) {
        Log.d(MainActivity.TAG, message);
    }

    public void logConstruct() {
        Log.d(MainActivity.TAG, "argA == " + argA);
        Log.d(MainActivity.TAG, "argB == " + argB);
    }
}
