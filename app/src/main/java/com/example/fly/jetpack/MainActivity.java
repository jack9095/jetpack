/*
 * https://blog.csdn.net/zyw0101/article/details/80399225
 */
package com.example.fly.jetpack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.fly.jetpack.module.CommonModule;
import com.example.fly.jetpack.presenter.LoginPresenter;
import com.example.fly.jetpack.view.ICommonView;
//import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ICommonView,Handler.Callback {

    @BindView(R.id.btn_login)
    Button btn;

//    @Inject
    LoginPresenter mLoginPresenter;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Log.e("MainActivity",msg.obj + " 呵呵");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        DaggerCommonCompnent.
//                builder().
//                commonModule(new CommonModule(this)).
//                build().
//                Inject(this);
    }

    @OnClick(R.id.btn_login)
    public void onClick(){
        Toast.makeText(this,"哈哈",Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("MainActivity"," 呵呵");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = "handler";
                handler.sendMessage(msg);
            }
        }).start();
//        handler.postDelayed()
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what){
            case 1:
                Log.e("MainActivity",message.obj + " 哈哈");
                break;
        }
        return false;
    }
}
