package com.jahanbabu.mvpdemo.login

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

        fun navigateToGoogleSigninIntent()

        fun navigateToMainScreen()
    }

    interface Presenter : BasePresenter {

        fun signInClick()

        fun handelUser(u: FirebaseUser?)

        fun firebaseAuthResult(b: Boolean)

    }
}
