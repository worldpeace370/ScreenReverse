package com.lebron.screenreverse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**使用onSaveInstanceState()和onRestoreInstanceState()进行数据保存与恢复
 * 不考虑加载时，进行旋转的情况，有意的避开这种情况，后面例子会介绍解决方案
 * 如果在加载的时候，进行旋转，则会发生错误，异常退出(退出原因：dialog.dismiss()时发生NullPointException，
 * 因为与当前对话框绑定的FragmentManager为null)
 */
public class SavedInstanceStateUsingActivity extends AppCompatActivity {
    private static final String TAG = "SavedInstance";
    private ArrayList<String> mDatas;
    private LoadingDialog mLoadingDialog;
    private LoadDataAsyncTask mLoadDataAsyncTask;
    private ListView mListView;
    private ListAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: 执行了");
        setContentView(R.layout.activity_saved_instance_state_using);
        mListView = (ListView) findViewById(R.id.listView);
        initData(savedInstanceState);
    }

    /**
     * 不利用savedInstanceState进行数据保存的写法
     */
    private void initDataWithoutBundle(){
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(getFragmentManager(), "LoadingDialog");
        mLoadDataAsyncTask = new LoadDataAsyncTask();
        mLoadDataAsyncTask.execute();
    }

    private void initData(Bundle savedInstanceState) {
        //Activity重新启动时，从之前Activity销毁时保存的数据里面取数据
        //Activity销毁时，其里面的数据也跟着销毁，可以用savedInstanceState进行系统保存
        if (savedInstanceState != null){
            mDatas = savedInstanceState.getStringArrayList("mDatas");
        }
        if (mDatas == null){//加载数据，显示加载对话框
            mLoadingDialog = new LoadingDialog();
            mLoadingDialog.show(getFragmentManager(), "LoadingDialog");
            mLoadDataAsyncTask = new LoadDataAsyncTask();
            mLoadDataAsyncTask.execute();
        }else {//不用加载数据，初始化适配器，将数据填充到ListView中
            initAdapter();
        }
    }

    /**
     * 在onCreate()方法第二次及以后第三次四次等等执行的时候，在onCreate()方法之后执行
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState 执行了");
    }

    /**
     * 用于在活动执行onDestroy()方法前进行数据的保存
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState 执行了");
        outState.putSerializable("mDatas", mDatas);
    }

    /**
     * 将数据填充到ListView
     */
    private void initAdapter() {
        mListAdapter = new ArrayAdapter<>(SavedInstanceStateUsingActivity.this,
                android.R.layout.simple_list_item_1, mDatas);
        mListView.setAdapter(mListAdapter);
//        mListView.onSaveInstanceState();调用View的onSaveInstanceState ()，返回Parcelable对象
    }
    private ArrayList<String> generateTimeConsumingDatas(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(Arrays.asList("通过Fragment保存大量数据",
                "onSaveInstanceState保存数据",
                "getLastNonConfigurationInstance已经被弃用", "RabbitMQ", "Hadoop",
                "Spark"));
    }
    private class LoadDataAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {//子线程
            mDatas = generateTimeConsumingDatas();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {//主线程
            super.onPostExecute(aVoid);
            mLoadingDialog.dismiss();
            initAdapter();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 执行了");
    }
}
