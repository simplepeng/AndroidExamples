package com.simple.example_fragment_proxy_activity_result;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class ProxyFragment extends Fragment {

    private Builder mBuilder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mBuilder == null) return;

        Intent intent = new Intent(context, mBuilder.mToActivity);
        startActivityForResult(intent, mBuilder.mRequestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("simple", "onActivityResult");
        if (mBuilder != null && mBuilder.mOnResultListener != null) {
            mBuilder.mOnResultListener.onActivityResult(requestCode, resultCode, data);
        }


        getFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Builder newBuilder(FragmentActivity activity) {
        if (mBuilder == null) {
            mBuilder = new Builder(activity, this);
        }
        return mBuilder;
    }

    public static class Builder {

        private FragmentActivity mWithActivity;
        private Class<?> mToActivity;
        private int mRequestCode;
        private OnResultListener mOnResultListener;
        private ProxyFragment mFragment;

        public Builder(FragmentActivity withActivity, ProxyFragment fragment) {
            this.mWithActivity = withActivity;
            mFragment = fragment;
        }

        public Builder setToActivity(Class<?> clz) {
            mToActivity = clz;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        public Builder startActivityForResult(OnResultListener listener) {
            mOnResultListener = listener;
            return this;
        }

        public void start() {

            FragmentManager manager = mWithActivity.getSupportFragmentManager();
            manager.beginTransaction()
                    .add(mFragment, "proxy")
                    .commitNowAllowingStateLoss();
            manager.executePendingTransactions();

        }
    }

}
