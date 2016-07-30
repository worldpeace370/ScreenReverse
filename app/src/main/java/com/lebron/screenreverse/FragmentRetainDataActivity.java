package com.lebron.screenreverse;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * 将下载而来的mBitmap通过mRetainedFragment进行保存，因为对象引用的关系
 * mBitmap的内容不会被回收。而mRetainedFragment由于保存进了fragmentManager
 * 所以尽管Activity销毁了(该类中的实例都会被销毁)，但是仍然能通过fragmentManager.findFragmentByTag()，
 * 恢复mRetainedFragment的实例，进而得到之前保存的mBitmap对象(需要在RetainedFragment
 * 中设置setRetainInstance(true))
 *
 * 这里也没有考虑加载时旋转屏幕，问题与上面的一致
 * 如果在加载的时候，进行旋转，则会发生错误，异常退出(退出原因：dialog.dismiss()时发生NullPointException，
 * 因为与当前对话框绑定的FragmentManager为null)
 */
public class FragmentRetainDataActivity extends AppCompatActivity {
    private static final String TAG = "FragmentRetainData";
    private RetainedFragment mRetainedFragment;
    private LoadingDialog mLoadingDialog;
    private Bitmap mBitmap;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate 执行了");
        setContentView(R.layout.activity_fragment_retain_data);

        // find the RetainedFragment on activity restarts
        FragmentManager fragmentManager = getSupportFragmentManager();
        mRetainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag("data");
        if (mRetainedFragment == null){
            Log.i(TAG, "mRetainedFragment 新创建！");//不管onCreate()执行几次，mRetainedFragment只创建一次
            mRetainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(mRetainedFragment, "data").commit();
        }
        mBitmap = mRetainedFragment.getData();
        initData();
    }

    /**
     * 初始化mBitmap数据，如果从mRetainedFragment获得的为null，则从网络下载
     */
    private void initData() {
        mImageView = (ImageView) findViewById(R.id.imageView);
        if (mBitmap == null){//从网络下载然后保存在mRetainedFragment中
            Log.i(TAG, "mBitmap 从网络加载！");
            mLoadingDialog = new LoadingDialog();
            mLoadingDialog.show(getFragmentManager(), "loading");
            RequestQueue requestQueue = Volley.newRequestQueue(FragmentRetainDataActivity.this);
            ImageRequest imageRequest = new ImageRequest("http://h.hiphotos.baidu.com/zhidao/pic/item/d439b6003af33a87aee02645c55c10385243b5db.jpg",
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            mBitmap = response;
                            mImageView.setImageBitmap(mBitmap);
                            mLoadingDialog.dismiss();
                            //将下载而来的mBitmap通过mRetainedFragment进行保存，因为对象引用的关系
                            //mBitmap的内容不会被回收。而mRetainedFragment由于保存进了fragmentManager
                            //所以尽管Activity销毁了，但是仍然能恢复mRetainedFragment的实例
                            mRetainedFragment.setData(mBitmap);
                        }
                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(FragmentRetainDataActivity.this, "图片资源错误", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(imageRequest);
        }else {
            if (mImageView != null) {
                mImageView.setImageBitmap(mBitmap);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy 执行了");
        //下面这句话不加上也行，在下载完成后已经设置过了
        mRetainedFragment.setData(mBitmap);
    }
}
