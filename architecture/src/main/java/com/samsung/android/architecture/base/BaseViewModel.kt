package com.samsung.android.architecture.base

import androidx.lifecycle.*

import com.samsung.android.architecture.event.SingleLiveEvent
import com.samsung.android.architecture.ext.injectObject
import com.samsung.android.architecture.ext.logD
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel(){
    private val mCompositeDisposable by lazy { CompositeDisposable() }

     val viewModelJob = SupervisorJob()
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var progressLiveEvent = SingleLiveEvent<Boolean>()
    var errorMessage = SingleLiveEvent<String>()
    var exceptionError: AppException? = null
    protected val _state = MutableLiveData<UIState>()

    val state: LiveData<UIState>
        get() = _state

     protected val _clickEvent = SingleLiveEvent<UIState>()

    val clickEvent: LiveData<UIState>
        get() = _clickEvent
    
     val logger: ILogger by injectObject()

    val handlerCoroutineException = CoroutineExceptionHandler { _, exception ->
        manageCoroutineException(exception)
    }

    open fun manageCoroutineException(exception: Any){
        viewModelScope.launch {
            if (exception is AppException) {

                errorMessage.value = exception.message

            } else if (exception is Throwable){
                errorMessage.value = exception.localizedMessage
            }
            errorMessage?.value?.let {
                logD("manageCoroutineException", it)
            }

            progressLiveEvent.value = false
        }

    }

    protected fun showProgress(){
        progressLiveEvent.value = true
    }

    protected fun hideProgress() {
        progressLiveEvent.value = false
    }

    open fun manageException(exception: Any) {
        progressLiveEvent.value = false
    }

    open fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        logD("onCleared","Enter")
        mCompositeDisposable?.clear()
        viewModelJob.cancel()

    }
    open fun onDestroyView() {}

}