package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private var backButtonCount = 0


    // Request code for launching the sign in Intent.
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        fab.hide()
        initializeListeners()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var backButtonCount = 0
        if (backButtonCount >= 1) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.back_button_exit_toast), Toast.LENGTH_SHORT)
                .show()
            backButtonCount++
            super.onBackPressed()
        }
    }

    private fun initializeListeners() {
        authListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            Log.d(Constants.TAG, "User: $user")
            if (user == null) {
                launchLoginUI()
            } else {
                switchToMainActivity(user.uid)
            }
        }
    }

    private fun switchToMainActivity(uid: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.UID, uid)
        finish()
        startActivity(intent)
    }

    private fun launchLoginUI() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            //AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val loginIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LoginTheme)
            .setLogo(R.mipmap.ic_launcher)
            .build()

        startActivityForResult(
            loginIntent,
            RC_SIGN_IN
        )
    }

}
