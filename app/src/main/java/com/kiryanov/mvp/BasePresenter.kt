package com.kiryanov.mvp

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

    private val mCompositeDisposable = CompositeDisposable()

    protected open fun Disposable.unsubscribeOnDestroy() = mCompositeDisposable.add(this)

    override fun onDestroy() {
        mCompositeDisposable.dispose()
        super.onDestroy()
    }
}