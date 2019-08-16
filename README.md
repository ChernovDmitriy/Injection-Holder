# Injection Holder
![VERSION](https://api.bintray.com/packages/chernovdmitriy/InjectionHolder/InjectionHolder/images/download.svg)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)

`Injection Holder` is a lightweight library that helps to to stay alive _DI components_ (sush as Dagger2 components) and any other objects. 
It automatically works in _Activity/Fragment_ (with LifecycleCallback help)

That's not all! :) You can use it in your _custom views_ and any objects, that can be recreate

## Capabilities
`Injection Holder` has next features:
- _DI Component_ stay alive when _Activity/Fragment_ recreated
- Automatic creating and destroying of _DI component_ in Activity/Fragment without boilerplate code
- Also available for using in custom views
- Support of _AppCompat_ & _AndroidX_
- Can use in _Java_ modules

## Getting started

**AndroidX**

```gradle
implementation 'com.github.chernovdmitriy.injectionholder:androidx:LATEST_VERSION'
```
First of all, you should register Activity/Fragment lifecycle callbacks at your application start

Then you can just implements `ComponentOwner<DiComponentType>` in your Fragment/Activity

If you use _Java_ and/or Activity/Fragment instances of one class in one time, than you should overrides method `getComponentKey()` of `ComponentOwner`

Let's look at the sample
```kotlin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        InjectionHolderX.init(this)
    }
}

class MyFragment : Fragment(), ComponentOwner<SubComponent> {

     override fun inject(subComponent: SubComponent) = subComponent.inject(this)
 
     override fun provideComponent(): SubComponent {
         return InjectionHolderX.instance
             .findComponent(ParentComponent::class.java)
             .subComponentBuilder()
             .build()
     }
}

class SomeFeatureBaseFragment : Fragment(), ComponentOwner<SubOfSubComponent> {

    override fun inject(subOfSubComponent: SubOfSubComponent) = subOfSubComponent.inject(this)
 
    override fun provideComponent(): SubOfSubComponent {
        return InjectionHolderX.instance
            .findComponent(SubComponent::class.java)
            .subOfSubComponentBuilder()
            .build()
    }
    
    override fun getComponentKey() = arguments?.getString(SomeFeatureData.EXTRA) ?: javaClass.toString()

}

```

And it's all what you need! You're welcome ;)

**AppCompat**

```gradle
implementation 'com.github.chernovdmitriy.injectionholder:appcompat:LATEST_VERSION'
```

Using of _AppCompat_ is idential to _AndroidX_ except _InjectionHolder_ object. You just call `InjectionHolderAppCompat` instead of `InjectionHolderX`

**Java/Kotlin**
```gradle
implementation 'com.github.chernovdmitriy.injectionholder:core:LATEST_VERSION'
```

You should realize class `InjectionHolder` and use it in your application instance
Your components will be stay alive automatically (the logic of it you should realize in `LifecycleCallbacksRegistry`)


## License
```
The MIT License (MIT)

Copyright (c) 2019 Chernov Dmitriy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```