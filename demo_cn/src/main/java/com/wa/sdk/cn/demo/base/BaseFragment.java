package com.wa.sdk.cn.demo.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import com.wa.sdk.cn.demo.widget.LoadingDialog;


/**
 * Fragment基类
 * Created by yinglovezhuzhu@gmail.com on 2015/10/30.
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

    protected LoadingDialog mLoadingDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 取消异步线程任务
     * @param task
     */
    protected void cancelTask(AsyncTask task) {
        if(null != task && !task.isCancelled()) {
            task.cancel(true);
            task = null;
        }
    }

    /**
     * 显示LoadingDialog
     * @param message
     * @param cancelable
     * @param canceledOnTouchOutside
     * @param cancelListener
     * @return
     */
    public LoadingDialog showLoadingDialog(String message,
                                           boolean cancelable,
                                           boolean canceledOnTouchOutside,
                                           DialogInterface.OnCancelListener cancelListener) {
        mLoadingDialog = LoadingDialog.showLoadingDialog(getActivity(), message,
                cancelable, canceledOnTouchOutside, cancelListener);
        return mLoadingDialog;
    }

    /**
     * 显示LoadingDialog
     * @param message
     * @param cancelListener
     * @return
     */
    public LoadingDialog showLoadingDialog(String message,
                                           DialogInterface.OnCancelListener cancelListener) {
        return showLoadingDialog(message, true, false, cancelListener);
    }

    /**
     * 隐藏LoadingDialog
     */
    public void cancelLoadingDialog() {
        if(null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.cancel();
        }
        mLoadingDialog = null;
    }

    /**
     * 显示一个短Toast
     * @param text
     */
    protected void showShortToast(CharSequence text) {
//        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        View view = getActivity().findViewById(android.R.id.content).getRootView();
        Snackbar.make(view,text,Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示一个短Toast
     * @param resId
     */
    protected void showShortToast(int resId) {
//        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
        View view = getActivity().findViewById(android.R.id.content).getRootView();
        Snackbar.make(view,resId,Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 显示一个长Toast
     * @param text
     */
    protected void showLongToast(CharSequence text) {
//        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        View view = getActivity().findViewById(android.R.id.content).getRootView();
        Snackbar.make(view,text,Snackbar.LENGTH_LONG).show();
    }

    /**
     * 显示一个长Toast
     * @param resId
     */
    protected void showLongToast(int resId) {
//        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
        View view = getActivity().findViewById(android.R.id.content).getRootView();
        Snackbar.make(view,resId,Snackbar.LENGTH_LONG).show();
    }

    /**
     * 获取资源id，如果没有找到，返回0
     * @param name
     * @param defType
     * @return
     */
    protected int getIdentifier(String name, String defType) {
        return getResources().getIdentifier(name, defType, getActivity().getPackageName());
    }

    protected int getResourceColor(int resId) {
        return getResources().getColor(resId);
    }




    /**
     * 退出<br/><br/>
     */
    protected void exit() {
        Activity activity = getActivity();
        if(activity instanceof BaseActivity) {
            ((BaseActivity) activity).exit();
        }
    }
    @Override
    public void onClick(View v) {

    }
}
