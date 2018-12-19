package com.simple.example_fragment_proxy_activity_result;

import android.support.v4.app.FragmentActivity;

public class Proxy {

    public static ProxyFragment.Builder with(FragmentActivity activity) {
        ProxyFragment fragment = new ProxyFragment();
        return fragment.newBuilder(activity);
    }


}
