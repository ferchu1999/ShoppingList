package com.example.supermarketapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.example.supermarketapp.R
import com.example.supermarketapp.activities.fragments.RegisterFragment
import com.example.supermarketapp.activities.fragments.ResetPasswordFragment
import com.example.supermarketapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val t: Thread = object : Thread() {
            override fun run() {
                buttonLogin()
                openFragmentRegister()
            }
        }
        t.start()
        openFragmentPasswordReset()

        lifecycleScope.launch {
            loadImage()
            withContext(Dispatchers.IO){
                session()
            }
        }

    }


    override fun onStart() {
        super.onStart()
        bindingMain.root.visibility = View.VISIBLE
    }


    private fun buttonLogin() {

        bindingMain.mbLogin.setOnClickListener {

            if (bindingMain.tieEmail.text!!.isNotEmpty() &&
                bindingMain.tiePassword.text!!.isNotEmpty()
            ) {

                auth.signInWithEmailAndPassword(
                    bindingMain.tieEmail.text.toString().trim(),
                    bindingMain.tiePassword.text.toString().trim()
                )
                    .addOnCompleteListener { login ->
                        if (login.isSuccessful) {
                            productActivity()
                        } else {
                            Toast.makeText(
                                this, "Email o contraseña incorrectos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this, "Debes completar los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if(email != null){
            bindingMain.root.visibility = View.INVISIBLE
            productActivity()
        }
    }

    private fun productActivity() {
        val intent = Intent(this, ProductList::class.java)
        startActivity(intent)
    }

    private fun openFragmentPasswordReset() {
        bindingMain.tvForgotPassword.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack(null)
                replace<ResetPasswordFragment>(bindingMain.fcvFragmentResetPassword.id)
            }
            invisibleViews()
        }
    }


    private fun openFragmentRegister() {

        bindingMain.tvRegister.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack(null)
                replace<RegisterFragment>(bindingMain.fcvFragment.id)
            }
            invisibleViews()
        }
    }

    private fun invisibleViews() {
        bindingMain.mbLogin.visibility = View.INVISIBLE
        bindingMain.viewLogin.visibility = View.INVISIBLE
        bindingMain.llButtons.visibility = View.INVISIBLE
        bindingMain.tvRegister.visibility = View.INVISIBLE
        bindingMain.ivLoginImage.visibility = View.INVISIBLE
        bindingMain.tvForgotPassword.visibility = View.INVISIBLE
        bindingMain.tilEmail.visibility = View.INVISIBLE
        bindingMain.tilPassword.visibility = View.INVISIBLE
    }


    private fun loadImage() {
        //Main image
        val url = "https://estaticosgn-cdn.deia.eus/clip/" +
                "ec4ec73b-7ce4-411e-b56a-d3bcabcec5bf_16-9-discover-aspect-ratio_default_0.jpg"

        Picasso.get().load(url)
            .resize(250, 200)
            .centerCrop().into(bindingMain.ivLoginImage)


        //Icons images
        val google = "https://cdn-icons-png.flaticon.com/512/2991/2991148.png"
        Picasso.get().load(google).resize(100, 100).centerCrop()
            .into(bindingMain.ibGoogle)

        val twitter = "https://png.pngtree.com/png-vector/20221018/ourmid" +
                "/pngtree-twitter-social-media-round-icon-png-image_6315985.png"
        Picasso.get().load(twitter).resize(100, 100).centerCrop()
            .into(bindingMain.ibTwitter)

        val facebook = "https://cdn1.iconfinder.com/data/icons/logotypes/32/" +
                "circle-facebook_-512.png"
        Picasso.get().load(facebook).resize(100, 100).centerCrop()
            .into(bindingMain.ibFacebook)
    }



}




