# Injection Holder
![VERSION](https://api.bintray.com/packages/chernovdmitriy/InjectionHolder/InjectionHolder/images/download.svg)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)

_Injection Holder_ is a lightweight library that helps to to save _DI components_ (like Dagger2 components) and any other objects. 
Also it can use for saving any objects in _Activity/Fragment_ (with LifecycleCallback help)
That's not all! You can use it in _custom views_ and another not platform objects, that can recreate
_Lightweight library without code generation

## Capabilities
_Injection Holder_ has next features:
- _DI Component_ stay alive when _Activity/Fragment_ recreated
- Automatic creating and destroying of DI component in Activity/Fragment without boilerplate code
- Also available for using in custom views
- Support of _AppCompat_ & _AndroidX_
- Can use in _Java_ modules

## Getting started

**AndroidX**

```gradle
implementation 'com.github.chernovdmitriy.injectionholder:androidx:LATEST_VERSION'

```
First of all, you should register Activity/Fragment lifecycle callbacks at your application start

Then you can just implements ComponentOwner<DiComponentType> in your Fragment/Activity

If you use Java and/or Activity/Fragment instances of one class in one time, than you should overrides method _getComponentKey()_ of _ComponentOwner_

Let's look at the sample
```kotlin

class MyApplication : Application() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

class BaseFragment : Fragment(), ComponentOwner<SubComponent> {

    override fun inject(subComponent: SubComponent) = subComponent.inject(this)
 
    override fun provideComponent(): SubComponent {
        return InjectionHolderX.instance
            .findComponent(ParentComponent::class.java)
            .subComponentBuilder()
            .build()
    }
    
    override fun getComponentKey() = arguments?.getString(SomeData.EXTRA) ?: javaClass.toString()

}

```

And it's all what you need! You're welcome ;)

**AppCompat**

```gradle
implementation 'com.github.chernovdmitriy.injectionholder:appcompat:LATEST_VERSION'
```

Using of _AppCompat_ is idential to _AndroidX_ except InjectionHolder object. You just call _InjectionHolderAppCompat_ instead of _InjectionHolderX_

**Java/Kotlin**
```gradle
implementation 'com.github.chernovdmitriy.injectionholder:core:LATEST_VERSION'
```

You should realize class _InjectionHolder_ and use it in your application instance
Your components will be store automatically (the logic of it you should realize in _LifecycleCallbacksRegistry_)


## License
```
The MIT License (MIT)

Copyright (c) 2016 Arello Mobile

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