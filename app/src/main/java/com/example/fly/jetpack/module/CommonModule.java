package com.example.fly.jetpack.module;

import com.example.fly.jetpack.annotation.ActivityScope;
import com.example.fly.jetpack.view.ICommonView;
//import dagger.Module;
//import dagger.Provides;

//@Module
public class CommonModule {

    private ICommonView iview;

    public CommonModule(ICommonView iview) {
        this.iview = iview;
    }

//    @Provides
    @ActivityScope
    public ICommonView provideICommonView() {
        return this.iview;
    }
}
