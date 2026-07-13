package com.sweethome.base

import androidx.annotation.CallSuper
import com.sweethome.data.CatalogRepository

open class MockPresenter<T: MvpView> constructor(
    protected val catalogRepository: CatalogRepository
) {

    protected var mvpView: T? = null

    @CallSuper
    open fun attach(mvpView: T) {
        this.mvpView = mvpView
    }

    open fun detach() {
        this.mvpView = null
    }
}
