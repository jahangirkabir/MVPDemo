package com.jahanbabu.mvpdemo.Login

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jahanbabu.mvpdemo.Home.MainActivity
import com.jahanbabu.mvpdemo.R
import com.jahanbabu.mvpdemo.util.EspressoIdlingResource
import com.jahanbabu.mvpdemo.util.showSnackBar
import com.jahanbabu.mvpdemo.util.showToast

/**
 * Main UI for the statistics screen.
 */
class LoginFragment : Fragment(), LoginContract.View {

    override lateinit var presenter: LoginContract.Presenter

    override val isActive: Boolean
        get() = isAdded

    override fun showProgress() {
        submitButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        submitButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun showLoginError(message: String) {
        view?.showToast(activity!!.applicationContext, message, Toast.LENGTH_LONG)
        submitButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun showLoginComplete(message: String) {
        view?.showToast(activity!!.applicationContext, message, Toast.LENGTH_LONG)
        progressBar.visibility = View.GONE
    }

    override fun navigateToMainScreen() {
        startActivity(Intent(activity, MainActivity::class.java))
        activity!!.finish()
    }

    lateinit var submitButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "GoogleActivity"
    private val RC_SIGN_IN = 9001

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.login_fragment, container, false)

        initProgressBar()

        // [START config_signin]
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(mContext.getString(R.string.web_client_id))
//            .requestIdToken("456602514519-hu7ionskb4hk4k136rl1um25f88nvcu1.apps.googleusercontent.com")
            .requestIdToken("456602514519-2o166vtu3t5j9k0gpqcc4f439baj8j8n.apps.googleusercontent.com")
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(activity!!.applicationContext, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        submitButton = root.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
//            presenter.handelLogin(activity!!.applicationContext)
//            presenter.handelLogin(activity!!.applicationContext)

            showProgress()

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e("LoginPresenter", "Google sign in failed", e)
                showLoginError("Google sign in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement() // Set app as idle.
                }
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    presenter.handelUser(user)
                    showLoginComplete("Sign In Successful")
                    navigateToMainScreen()
                } else {
                    // If sign in fails, display a message to the user.
                    showLoginError("SignInWithCredential:failure")
                }
            }
    }

    private fun initProgressBar() {
        progressBar = ProgressBar(activity, null, android.R.attr.progressBarStyleLarge)
        progressBar!!.isIndeterminate = true

        val relativeLayout = RelativeLayout(activity)
        relativeLayout.gravity = Gravity.CENTER
        relativeLayout.addView(progressBar)

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        progressBar!!.visibility = View.INVISIBLE

        activity!!.addContentView(relativeLayout, params)
    }

//    private fun initProgressBar() {
//        progressBar = ProgressBar(activity, null, android.R.attr.progressBarStyleSmall)
//        progressBar.setIndeterminate(true)
//        val params = RelativeLayout.LayoutParams(
//            Resources.getSystem().getDisplayMetrics().widthPixels, 250
//        )
//        params.addRule(RelativeLayout.CENTER_IN_PARENT)
//        activity!!.addContentView(progressBar, params)
//        progressBar.visibility = View.GONE
//    }

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
