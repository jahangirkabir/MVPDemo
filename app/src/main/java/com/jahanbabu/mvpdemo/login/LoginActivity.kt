package com.jahanbabu.mvpdemo.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jahanbabu.mvpdemo.R
import com.jahanbabu.mvpdemo.util.replaceFragmentInActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val loginFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as LoginFragment?
            ?: LoginFragment.newInstance().also {
                replaceFragmentInActivity(it, R.id.contentFrame)
            }

        LoginPresenter(this@LoginActivity, loginFragment)
//        LoginPresenter(this@LoginActivity, loginFragment, Injection.provideTasksRepository(applicationContext))
    }

}
