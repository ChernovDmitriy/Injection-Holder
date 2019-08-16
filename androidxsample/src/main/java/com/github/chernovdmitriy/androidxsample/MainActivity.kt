package com.github.chernovdmitriy.androidxsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
//
//interface ParentComponent {
//    fun subComponentBuilder(): SubComponentBuilder
//}
//
//interface SubComponentBuilder {
//    fun build(): SubComponent
//}
//
//interface SubComponent {
//    fun inject(mainActivity: MainActivity)
//}
//
//class SomeFragment: Fragment(), ComponentOwner<SubComponent> {
//    override fun inject(subComponent: SubComponent) = subComponent.inject(this)
//
//    override fun provideComponent(): SubComponent {
//        return InjectionHolderX.instance
//            .findComponent(ParentComponent::class.java)
//            .subComponentBuilder()
//            .build()
//    }
//
//    override fun getComponentKey() = arguments?.getString(SomeData.EXTRA) ?: javaClass.toString()
//}