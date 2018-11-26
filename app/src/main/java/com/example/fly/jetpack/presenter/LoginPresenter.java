package com.example.fly.jetpack.presenter;

import android.content.Context;
import android.widget.Toast;

import com.example.fly.jetpack.bean.User;
import com.example.fly.jetpack.view.ICommonView;

public class LoginPresenter {

    ICommonView iView;

    public LoginPresenter(ICommonView iView){
        this.iView = iView;
    }

    public void login(User user){
        Context mContext = iView.getContext();
        Toast.makeText(mContext,"login......",Toast.LENGTH_SHORT).show();
    }

}
