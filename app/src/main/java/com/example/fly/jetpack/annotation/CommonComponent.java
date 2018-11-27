package com.example.fly.jetpack.annotation;

import com.example.fly.jetpack.MainActivity;
import com.example.fly.jetpack.module.CommonModule;
//import dagger.Component;

@ActivityScope
//@Component(modules = CommonModule.class)
public interface CommonComponent {

    void inject(MainActivity activity);

}
