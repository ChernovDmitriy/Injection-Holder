package com.github.chernovdmitriy.injectionholderappcompat

import android.os.Bundle
import com.github.chernovdmitriy.injectionholdercore.api.RestorableComponentOwner

interface BundleComponentOwner<Component> : RestorableComponentOwner<Bundle, Component>