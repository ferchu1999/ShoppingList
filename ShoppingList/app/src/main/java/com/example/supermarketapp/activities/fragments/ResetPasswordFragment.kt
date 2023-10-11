package com.example.supermarketapp.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.supermarketapp.R
import com.example.supermarketapp.activities.MainActivity
import com.example.supermarketapp.databinding.FragmentResetPasswordBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ResetPasswordFragment : Fragment() {

    private var mActivity: MainActivity? = null
    private lateinit var fragmentResetPasswordBinding: FragmentResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResetPasswordBinding = FragmentResetPasswordBinding
            .inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        return fragmentResetPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as? MainActivity

        sendPasswordReset()
    }


    private fun sendPasswordReset() {
        // [START send_password_reset]
       fragmentResetPasswordBinding.mbResetPassword.setOnClickListener {
           val email = fragmentResetPasswordBinding.tieEmailUserReset.text.toString().trim()
           auth.sendPasswordResetEmail(email)
               .addOnCompleteListener { send ->
                   if (send.isSuccessful) {
                       Toast.makeText(context, "Email enviado",
                           Toast.LENGTH_SHORT).show()
                       clearEmail()
                   }
               }
       }
        // [END send_password_reset]
    }

    private fun clearEmail(){
        fragmentResetPasswordBinding.tieEmailUserReset.text!!.clear()
    }

    override fun onDestroy() {
        this.exitTransition = FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
        mActivity?.findViewById<MaterialButton>(R.id.mb_login)?.visibility = View.VISIBLE
        mActivity?.findViewById<TextView>(R.id.tv_register)?.visibility = View.VISIBLE
        mActivity?.findViewById<LinearLayout>(R.id.ll_buttons)?.visibility = View.VISIBLE
        mActivity?.findViewById<View>(R.id.view_login)?.visibility = View.VISIBLE
        mActivity?.findViewById<TextView>(R.id.tv_forgot_password)?.visibility = View.VISIBLE
        mActivity?.findViewById<TextInputLayout>(R.id.til_email)?.visibility = View.VISIBLE
        mActivity?.findViewById<TextInputLayout>(R.id.til_password)?.visibility = View.VISIBLE
        super.onDestroy()
    }
}