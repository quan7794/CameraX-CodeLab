package com.samsung.android.architecture.ext

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.createViewModelLazy
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.architecture.base.ILogger
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.get
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.getKoin
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.androidx.viewmodel.ViewModelStoreOwnerProducer
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.androidx.viewmodel.scope.BundleDefinition
import org.koin.androidx.viewmodel.scope.emptyState
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

const val ID_SCOPE_TV_V3 = "tv.v3"



//inline fun <reified T : ViewModel> Fragment.koinActivityViewModel(): Lazy<T> =
//    lazy {
//        getKoin()
//            .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
//            .getViewModel(requireActivity(), T::class) {
//                parametersOf(context)
//            }
//    }
//
//inline fun <reified T : ViewModel> Fragment.koinParentFragmentViewModel(): Lazy<T> =
//    lazy {
//        Log.d(this::class.java.simpleName, "koinParentFragmentViewMode ${requireParentFragment()::class.java}")
//        getKoin()
//            .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
//            .getViewModel(requireParentFragment(), T::class) {
//                parametersOf(context)
//            }
//    }
//
//inline fun <reified T : ViewModel> Fragment.koinStateViewModel(): Lazy<T> =
//    lazy {
//        getKoin()
//            .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
//            .getStateViewModel(this, T::class) {
//                parametersOf(context)
//            }
//    }


inline fun <reified T : ILogger> Any.getLazyLogger(): Lazy<T> =
    lazy {
        getKoin()
            .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
            .get<T>(T::class.java, null) { parametersOf(this) }
    }


inline fun <reified T : Any> Any.injectObject(): Lazy<T> =
    lazy {

        getKoin()
            .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
            .get<T>(T::class.java, null) { parametersOf(this) }
    }
inline fun <reified T : Any> Fragment.injectObjectParams(): Lazy<T> =
    lazy {
        getKoin()
            .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
            .get<T>(T::class.java, null) { parametersOf(requireActivity()) }
    }



inline fun <reified T : Any> Fragment.deleteScope(): Lazy<T> =
    lazy {
        getKoin()
            .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
            .get<T>(T::class.java, null) { parametersOf(this) }
    }

@OptIn(KoinInternalApi::class)
inline fun <reified T : ViewModel> Fragment.koinViewModel(
    qualifier: Qualifier? = null,
    noinline owner: ViewModelStoreOwnerProducer = { this },
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    val scope =  getKoin()
        .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
    return viewModels(ownerProducer = owner) {
        getViewModelFactory<T>(owner(), qualifier, parameters?:{ parametersOf(context)}, scope = scope)
    }
}

@OptIn(KoinInternalApi::class)
inline fun <reified T : ViewModel> Fragment.koinStateViewModel(
    qualifier: Qualifier? = null,
    noinline state: BundleDefinition = emptyState(),
    noinline owner: ViewModelStoreOwnerProducer = { this },
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    val scope =  getKoin()
        .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
    return createViewModelLazy(T::class, { viewModelStore }) {
        getViewModelFactory(owner(), T::class, qualifier, parameters, state = state, scope = scope)
    }
}

@OptIn(KoinInternalApi::class)
inline fun <reified T : ViewModel> ComponentActivity.koinViewModel(
    qualifier: Qualifier? = null,
    owner : ViewModelStoreOwner = this,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    val scope =  getKoin()
        .getOrCreateScope(ID_SCOPE_TV_V3, named(ID_SCOPE_TV_V3))
    return viewModels {
        getViewModelFactory<T>(owner, qualifier, parameters, scope = scope)
    }
}

inline fun <reified T : ViewModel> ComponentActivity.getViewModel(
    qualifier: Qualifier? = null,
    owner : ViewModelStoreOwner = this,
    noinline parameters: ParametersDefinition? = null,
): T {
    return viewModel<T>(qualifier, owner, parameters).value
}

