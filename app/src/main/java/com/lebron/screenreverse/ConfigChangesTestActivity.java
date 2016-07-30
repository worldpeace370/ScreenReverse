package com.lebron.screenreverse;

import android.app.DialogFragment;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 在AndroidManifest中进行属性设置：<activity
                                    android:name=".ConfigChangesTestActivity"
                                    android:configChanges="screenSize|orientation" >
                                    </activity>
  低版本的API只需要加入orientation，而高版本的则需要加入screenSize
  屏幕反转过程不会导致Activity的销毁与重新创建，但会回调onConfigurationChanged()方法
  最简单粗暴的方式，尽管屏幕不断地反转，但是生命周期不会重新执行
 */
public class ConfigChangesTestActivity extends AppCompatActivity {
    private static final String TAG = "ConfigChangesTest";
    private ListAdapter mAdapter;
    private ArrayList<String> mDatas;
    private DialogFragment mLoadingDialog;
    private LoadDataAsyncTask mLoadDataAsyncTask;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: 执行了");
        setContentView(R.layout.activity_config_changes_test);
        initData(savedInstanceState);
    }

    /**
     * 初始化数据
     */
    private void initData(Bundle savedInstanceState)
    {
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(getFragmentManager(), "LoadingDialog");
        mLoadDataAsyncTask = new LoadDataAsyncTask();
        mLoadDataAsyncTask.execute();
    }

    /**
     * 模拟耗时操作
     *
     * @return
     */
    private ArrayList<String> generateTimeConsumingDatas()
    {
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
        }
        return new ArrayList<String>(Arrays.asList("通过Fragment保存大量数据",
                "onSaveInstanceState保存数据",
                "getLastNonConfigurationInstance已经被弃用", "RabbitMQ", "Hadoop",
                "Spark"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        }else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            mDatas = generateTimeConsumingDatas();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mLoadingDialog.dismiss();
            initAdapter();
        }
    }

    private void initAdapter() {
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(ConfigChangesTestActivity.this,
                android.R.layout.simple_list_item_1, mDatas);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy()
    {
        Log.e(TAG, "onDestroy 执行了");
        super.onDestroy();
    }
}
