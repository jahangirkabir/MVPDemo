package com.jahanbabu.mvpdemo.Login

import com.google.firebase.auth.FirebaseUser
import com.jahanbabu.mvpdemo.BasePresenter
import com.jahanbabu.mvpdemo.BaseView

interface LoginContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun showProgress()

        fun hideProgress()

        fun showLoginError(s: String)

        fun showLoginComplete(s: String)

        fun navigateToMainScreen()
    }

    interface Presenter : BasePresenter {

        fun handelUser(u: FirebaseUser?)

    }
}
