package com.simple.example_fragment_proxy_activity_result;

import android.content.Intent;

public interface OnResultListener {
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
