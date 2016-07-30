package com.lebron.screenreverse;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wuxiangkun on 2016/7/25 17:35.
 * Contacts wuxiangkun@live.com
 */
public class MyAsyncTask extends AsyncTask<Void, Void, Void> implements LoadingDialog.OnDialogCanceledListener{
    private static final String TAG = "MyAsyncTask";
    private FixProblemsActivity activity;
//    private WeakReference<Activity> mWeakReference; 可以什么为弱引用，不过在此处不用也行，不会有内存泄漏
    /**
     * 是否完成
     */
    private boolean isCompleted;
    /**
     * 进度框
     */
    private LoadingDialog mLoadingDialog;
    private List<String> items;

    public MyAsyncTask(FixProblemsActivity activity)
    {
        this.activity = activity;
//        mWeakReference = new WeakReference<Activity>(activity);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.setOnDialogCanceledListener(this);
//        mLoadingDialog.setCancelable(false);
        //传入的activity的销毁，会导致activity.getFragmentManager()为空
//        FixProblemsActivity activity = (FixProblemsActivity) mWeakReference.get();
        if (activity != null){
            mLoadingDialog.show(activity.getFragmentManager(), "Loading");
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        items = loadingData();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        isCompleted = true;
        Log.i(TAG, "onPostExecute: 执行了");
        notifyActivityTaskCompleted();
        if (mLoadingDialog != null){
            mLoadingDialog.dismiss();
        }
    }

    private List<String> loadingData()
    {
        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
        return new ArrayList<>(Arrays.asList("通过Fragment保存大量数据",
                "onSaveInstanceState保存数据",
                "getLastNonConfigurationInstance已经被弃用", "RabbitMQ", "Hadoop",
                "Spark"));
    }

    public List<String> getItems()
    {
        return items;
    }

    /**
     * 任务执行完成，回调Activity中的方法，加载ListView数据
     */
    private void notifyActivityTaskCompleted(){
//        FixProblemsActivity activity = (FixProblemsActivity) mWeakReference.get();
        if (activity != null){
            activity.onTaskCompleted();
        }
    }

    /**
     * 设置Activity，因为Activity会一直变化
     *
     * @param activity
     */
    public void setActivity(FixProblemsActivity activity){
        // 如果上一个Activity销毁，将与上一个Activity绑定的DialogFragment销毁
        if (activity == null){//有可能在执行下面if时，下载任务执行完了(isCompleted为true)，没有创建新的mLoadingDialog
            if (mLoadingDialog != null){
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        }
//        mWeakReference.clear();
//        mWeakReference = new WeakReference<Activity>(activity);
        // 设置为当前的Activity
        this.activity = activity;
        // 开启一个与当前Activity绑定的等待框
        if (activity != null && !isCompleted){//新创建
            mLoadingDialog = new LoadingDialog();
            mLoadingDialog.setOnDialogCanceledListener(this);
//            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show(activity.getFragmentManager(), "Loading");
        }

        // 如果完成，通知Activity
        if (isCompleted){
            notifyActivityTaskCompleted();
        }
    }

    /**
     * 实现LoadingDialog类中定义的加载框取消接口方法，在这里实现
     * 进行异步任务的取消，并弹出吐司
     */
    @Override
    public void dialogCanceledListener() {
        this.cancel(true);
        Toast.makeText(activity.getApplicationContext(), "下载任务取消啦！", Toast.LENGTH_SHORT).show();
    }
}
