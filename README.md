# Injection Holder
![VERSION](https://api.bintray.com/packages/chernovdmitriy/InjectionHolder/InjectionHolder/images/download.svg)
[![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/MIT)

`Injection Holder` is a lightweight library that helps _DI components_ (such as Dagger2 components) to stay alive during Activity/Fragment lifecycle. 

Also you don't have to worry about storing your DI components and operations with them such as adding/getting/removing etc.

Finally, you can use it in any objects, that can be recreate

## Capabilities
`Injection Holder` has next features:
- Automated adding and clearing of _DI component_ in Activity/Fragment without boilerplate code
- _DI Component_ stay alive after _Activity/Fragment_ recreation
- Available for using in custom views
- Support of _AppCompat_ & _AndroidX_
- Can be used in _Java_ modules

## Getting started

**AndroidX**

```gradle
implementation 'com.github.chernovdmitriy.injectionholder:androidx:LATEST_VERSION'
```
Let's look at the sample

First of all, you will register Activity/Fragment lifecycle callbacks at launch of your application

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        InjectionHolderX.init(this)
    }
}
```

Then you just implements `ComponentOwner<DiComponentType>` interface in your Fragment/Activity
```kotlin
class MyFragment : Fragment(), ComponentOwner<SubComponent> {
    override fun inject(subComponent: SubComponent) = subComponent.inject(this)
 
    override fun provideComponent(): SubComponent {
        return InjectionHolderX
            .findComponent(ParentComponent::class.java)
            .subComponentBuilder()
            .build()
    }
}
```
Additionally, since __1.1.0__ version interface `BundleComponentOwner` has been added, which give access to Bundle of your savedInstanceState (if Activity/Fragment have been restored) at component providing.
In that case your code will looks similarly simple
```kotlin
class MyFragment : Fragment(), BundleComponentOwner<MyComponent> {
    override fun inject(myComponent: MyComponent) = myComponent.inject(this)
 
    override fun provideComponent(savedState: Bundle?): MyComponent {
        val savedStateString: String? = savedState?.getString(ARGUMENT_SAMPLE_KEY)
        return MyComponentProvider.getInstance(savedStateString).myComponent
    }
}
```

If you use _Java_ and/or Activity/Fragment instances of one class in one time, you should override method `getComponentKey()` of `ComponentOwner`

```kotlin
class SomeFeatureBaseFragment : Fragment(), ComponentOwner<SubOfSubComponent> {
    override fun inject(subOfSubComponent: SubOfSubComponent) = subOfSubComponent.inject(this)
 
    override fun provideComponent(): SubOfSubComponent {
        return InjectionHolderX
            .findComponent(SubComponent::class.java)
            .subOfSubComponentBuilder()
            .build()
    }
    
    override fun getComponentKey() = arguments?.getString(SomeFeatureData.EXTRA) ?: javaClass.toString()
}

```

And that's all what you need! 

You're welcome ;)


**AppCompat**

```gradle
implementation 'com.github.chernovdmitriy.injectionholder:appcompat:LATEST_VERSION'
```

Using of _AppCompat_ is identical to _AndroidX_ except _InjectionHolder_ object. You just call `InjectionHolderAppCompat` instead of `InjectionHolderX`


**Java/Kotlin**
```gradle
implementation 'com.github.chernovdmitriy.injectionholder:core:LATEST_VERSION'
```

You should provide subclass of `InjectionHolder` and use it in your application instance.
_DI components_ will be stayed alive automatically (This logic must be implements by `LifecycleCallbacksRegistry`)


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
