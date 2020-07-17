package com.github.chernovdmitriy.appcompatsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.chernovdmitriy.injectionholderappcompat.InjectionHolderAppCompat
import com.github.chernovdmitriy.injectionholdercore.api.ComponentOwner

class MainActivity : AppCompatActivity(), ComponentOwner<Any> {

    private lateinit var injectedAny: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InjectionHolderAppCompat.init(application)
    }

    override fun provideComponent(): Any {
        return Any()
    }

    override fun inject(t: Any) {
        injectedAny = t
    }
}
