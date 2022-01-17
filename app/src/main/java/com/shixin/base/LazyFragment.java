package com.shixin.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class LazyFragment extends Fragment {

    View rootView;

    boolean isViewCreated       = false;
    boolean currentVisibleState = false; //当前Fragment的可见状态

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }
        initView(rootView);
        isViewCreated = true;
        if (getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }
        return rootView;
    }

    protected abstract void initView(View rootView);

    protected abstract int getLayoutRes();


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewCreated) {
            if (!currentVisibleState && isVisibleToUser) {
                dispatchUserVisibleHint(true);
            } else if (currentVisibleState && !isVisibleToUser) {
                dispatchUserVisibleHint(false);
            }
        }
    }


    /**
     * 统一分发事件的可见性
     *
     * @param isVisible
     */
    private void dispatchUserVisibleHint(boolean isVisible) {
        if (currentVisibleState == isVisible) {
            return;
        }
        currentVisibleState = isVisible;
        if (isVisible) {
            onFragmentLoad();
        } else {
            onFragmentLoadStop();
        }
    }


    public void onFragmentLoadStop() {

    }

    public void onFragmentLoad() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentVisibleState && getUserVisibleHint()) {
            dispatchUserVisibleHint(false);
        }
    }
}
