package com.lebron.screenreverse;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**保存异步任务对象的Fragment
 * Created by wuxiangkun on 2016/7/26 15:17.
 * Contacts wuxiangkun@live.com
 */
public class OtherRetainedFragment extends Fragment{
    // data object we want to retain
    // 保存一个异步的任务
    private MyAsyncTask data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(MyAsyncTask data)
    {
        this.data = data;
    }

    public MyAsyncTask getData()
    {
        return data;
    }
}
