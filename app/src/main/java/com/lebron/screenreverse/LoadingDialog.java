package com.lebron.screenreverse;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**谷歌官方推荐用DialogFragment来代替Dialog
 * Created by wuxiangkun on 2016/7/25 17:20.
 * Contacts wuxiangkun@live.com
 */
public class LoadingDialog extends DialogFragment implements MyDialog.OnKeyDownPressedListener {
    private String mMsg = "Loading";
    private OnDialogCanceledListener mOnDialogCanceledListener;

    public void setOnDialogCanceledListener(OnDialogCanceledListener onDialogCanceledListener) {
        mOnDialogCanceledListener = onDialogCanceledListener;
    }

    public void setMsg(String msg)
    {
        this.mMsg = msg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.loadingdialog_layout, null);
        TextView title = (TextView) view
                .findViewById(R.id.id_dialog_loading_msg);
        title.setText(mMsg);
        MyDialog dialog = new MyDialog(getActivity(), R.style.dialog);
        dialog.setOnKeyDownPressedListener(this);
        dialog.setContentView(view);
        return dialog;
    }

    /**
     * 实现，MyDialog中按下返回键的接口方法，此处仍然不是自己处理，而是交给实现
     * OnDialogCanceledListener接口的类，在这里是MyAsyncTask.
     */
    @Override
    public void keyDownPressed() {
        if (mOnDialogCanceledListener != null){
            //接口回调到实现OnDialogCanceledListener的类中的该方法
            mOnDialogCanceledListener.dialogCanceledListener();
        }
    }

    public interface OnDialogCanceledListener{
        void dialogCanceledListener();
    }
}
