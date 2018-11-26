/*
 * https://blog.csdn.net/zyw0101/article/details/80399225
 */
package com.example.fly.jetpack;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.fly.jetpack.module.CommonModule;
import com.example.fly.jetpack.presenter.LoginPresenter;
import com.example.fly.jetpack.view.ICommonView;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ICommonView {

    @BindView(R.id.btn_login)
    Button btn;

    @Inject
    LoginPresenter mLoginPresenter;

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
}
