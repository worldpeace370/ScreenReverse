package com.lebron.screenreverse;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class FixProblemsActivity extends AppCompatActivity {
    private static final String TAG = "FixProblemsActivity";
    private OtherRetainedFragment mDataFragment;
    private MyAsyncTask mMyTask;
    private ListAdapter mAdapter;
    private List<String> mDatas;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: 执行了");
        setContentView(R.layout.activity_fix_problems);
        // find the retained fragment on activity restarts
        FragmentManager fragmentManager = getSupportFragmentManager();
        mDataFragment = (OtherRetainedFragment) fragmentManager.findFragmentByTag("data");
        // create the fragment and data the first time
        if (mDataFragment == null){
            // add the fragment
            mDataFragment = new OtherRetainedFragment();
            fragmentManager.beginTransaction().add(mDataFragment, "data").commit();
        }

        mMyTask = mDataFragment.getData();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (mMyTask != null){//从mDataFragment恢复出来的异步任务
            Log.i(TAG, "initData: mMTask != null");
            mMyTask.setActivity(this);
        }else {
            Log.i(TAG, "initData: mMTask == null");
            mMyTask = new MyAsyncTask(this);
            mDataFragment.setData(mMyTask);
            mMyTask.execute();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState(state);
        Log.i(TAG, "onRestoreInstanceState");
    }
    //该方法在 onPause()和onStop之间执行
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState: 执行了");
        //防止内存泄漏，因为mMyTask引用了该Activity，在销毁的时候不设置其为null的话，当前Activity不能销毁
        mMyTask.setActivity(null);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: 执行了");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMyTask.setActivity(this);
        Log.i(TAG, "onRestart: 执行了");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 执行了");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: 执行了");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: 执行了");
    }

    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * 回调
     */
    public void onTaskCompleted(){
        mDatas = mMyTask.getItems();
        mAdapter = new ArrayAdapter<>(FixProblemsActivity.this,
                android.R.layout.simple_list_item_1, mDatas);
        mListView = (ListView) findViewById(R.id.listView);
        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "FixProblemsActivity onKeyDown: 外面");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ){
            mMyTask.cancel(true);
            finish();
            Log.i(TAG, "FixProblemsActivity onKeyDown: 里面");
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
