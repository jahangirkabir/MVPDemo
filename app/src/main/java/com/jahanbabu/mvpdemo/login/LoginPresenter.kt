package com.jahanbabu.mvpdemo.login

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser

/**
 * Listens to user actions from the UI ([LoginFragment]), retrieves the data and updates
 * the UI as required.
 */
class LoginPresenter(
    val activity: LoginActivity,
    val loginView: LoginContract.View) : LoginContract.Presenter {

    override fun handelUser(user: FirebaseUser?) {
        //Save user or check or other business logic
        Log.e("LoginPresenter", "ID: ${user!!.uid}\nName: ${user.displayName}\nEmail: ${user.email}\nPhoto: ${user.photoUrl}")
    }

    init {
        loginView.presenter = this
    }

    lateinit var mContext: Context

    override fun start() {
        loginView.presenter = this
    }

}
