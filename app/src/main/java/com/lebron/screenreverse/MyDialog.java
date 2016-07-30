package com.lebron.screenreverse;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by wuxiangkun on 2016/7/26 21:29.
 * Contacts wuxiangkun@live.com
 */
public class MyDialog extends Dialog{
    private OnKeyDownPressedListener mOnKeyDownPressedListener;

    public void setOnKeyDownPressedListener(OnKeyDownPressedListener onKeyDownPressedListener) {
        mOnKeyDownPressedListener = onKeyDownPressedListener;
    }

    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**按下返回键时，取消加载对话框，并通过接口回调取消异步任务下载
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("MyDialog", "MyDialog onKeyDown: 外面");
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Log.i("MyDialog", "MyDialog onKeyDown: 里面");
            dismiss();
            if (mOnKeyDownPressedListener != null){
                //回调LoadingDialog中的实现方法，交给它处理
                mOnKeyDownPressedListener.keyDownPressed();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 定义按下返回键时，接口回调的方法，最终通过两级，交给MyAsyncTask处理
     */
    public interface OnKeyDownPressedListener{
        void keyDownPressed();
    }
}
