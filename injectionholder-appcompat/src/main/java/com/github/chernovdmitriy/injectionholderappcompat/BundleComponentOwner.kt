package com.github.chernovdmitriy.injectionholderappcompat

import android.os.Bundle
import com.github.chernovdmitriy.injectionholdercore.RestorableComponentOwner

interface BundleComponentOwner<Component> : RestorableComponentOwner<Bundle, Component>