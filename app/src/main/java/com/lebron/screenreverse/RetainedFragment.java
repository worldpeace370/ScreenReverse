package com.lebron.screenreverse;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**使用Fragment来保存对象，用于恢复数据
 * 如果重新启动你的Activity需要恢复大量的数据，重新建立网络连接，或者执行其他的密集型操作，
 * 这样因为配置发生变化而完全重新启动可能会是一个慢的用户体验。并且，
 * 使用系统提供的onSaveIntanceState()的回调中，使用Bundle来完全恢复你Activity的状态
 * 是可能是不现实的（Bundle不是设计用来携带大量数据的（例如bitmap），并且Bundle中的数据
 * 必须能够被序列化和反序列化），这样会消耗大量的内存和导致配置变化缓慢。在这样的情况下，
 * 当你的Activity因为配置发生改变而重启，你可以通过保持一个Fragment来缓解重新启动带来的负担。
 * 这个Fragment可以包含你想要保持的有状态的对象的引用。
 * 当Android系统因为配置变化关闭你的Activity的时候，你的Activity中被标识保持的fragments不会被销毁。
 * 你可以在你的Activity中添加这样的fragements来保存有状态的对象。
 * 在运行时配置发生变化时，在Fragment中保存有状态的对象
 * a) 继承Fragment，声明引用指向你的有状态的对象
 * b) 当Fragment创建时调用setRetainInstance(boolean)
 * c) 把Fragment实例添加到Activity中
 * d) 当Activity重新启动后，使用FragmentManager对Fragment进行恢复
 * Created by wuxiangkun on 2016/7/25 21:06.
 * Contacts wuxiangkun@live.com
 */
public class RetainedFragment extends Fragment{
    private static final String TAG = "RetainedFragment";
    // data object we want to retain
    private Bitmap data;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: 执行了");
    }

    // this method is only called once for this fragment
    // onCreate只会执行一次，在所属Activity不断重建的过程中
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: 执行了");
        // retain this fragment,会改变当前Fragment的生命周期,onDestroy()方法不会被执行
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: 执行了");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: 执行了");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: 执行了");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: 执行了");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: 执行了");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: 执行了");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: 执行了");
    }
    //在所属Activity不断重建的过程中onDestroy()不会被执行，
    //只有当 当前所属Activity退出的时候(非重建形式退出),onDestroy()被执行
    //表示当前用来保存数据的Fragment实例被从内存中销毁。在所属Activity重建的过程中，该实例不会被销毁
    //用来保存数据
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: 执行了");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: 执行了");
    }

    public void setData(Bitmap data)
    {
        this.data = data;
    }

    public Bitmap getData()
    {
        return data;
    }
}
