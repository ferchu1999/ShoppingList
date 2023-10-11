package com.example.supermarketapp.activities.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.supermarketapp.R
import com.example.supermarketapp.activities.MainActivity
import com.example.supermarketapp.databinding.FragmentRegisterBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class RegisterFragment : Fragment() {

    private var mActivity: MainActivity? = null
    private lateinit var fragmentRegistrationBinding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        fragmentRegistrationBinding = FragmentRegisterBinding
            .inflate(inflater, container, false)
        // Initialize Firebase Auth
        auth = Firebase.auth
        return fragmentRegistrationBinding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as? MainActivity

        val t: Thread = object : Thread() {
            override fun run() {
                registerForm()
            }
        }
        t.start()

        loadImage()
    }

    private fun loadImage() {
        val url = "https://estaticosgn-cdn.deia.eus/clip/" +
        "ec4ec73b-7ce4-411e-b56a-d3bcabcec5bf_16-9-discover-aspect-ratio_default_0.jpg"

        Picasso.get().load(url)
            .resize(250,200)
            .centerCrop().into(fragmentRegistrationBinding.ivRegisterImage)
    }


    private fun registerForm() {
        fragmentRegistrationBinding.mbRegisterButton.setOnClickListener {

            if (fragmentRegistrationBinding.tieNameUserRegister.text!!.isNotEmpty()
                && fragmentRegistrationBinding.tieEmailUser.text!!.isNotEmpty()
                && fragmentRegistrationBinding.tiePasswordUser.text!!.isNotEmpty()
                && fragmentRegistrationBinding.tieRepeatPasswordUser.text!!.isNotEmpty()
            ) {

                if (fragmentRegistrationBinding.tiePasswordUser.text.toString().trim()
                    == fragmentRegistrationBinding.tieRepeatPasswordUser.text.toString().trim()
                ) {

                    auth.createUserWithEmailAndPassword(
                        fragmentRegistrationBinding.tieEmailUser.text.toString().trim(),
                        fragmentRegistrationBinding.tiePasswordUser.text.toString().trim()

                    ).addOnCompleteListener { register ->
                            if (register.isSuccessful) {
                                clearFields()
                            } else {
                                println("Error")
                            }
                        }
                }else{
                    Toast.makeText(context, "Las contraseñas no coinciden",
                        Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Completa todos los campos",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        fragmentRegistrationBinding.tieNameUserRegister.text!!.clear()
        fragmentRegistrationBinding.tieEmailUser.text!!.clear()
        fragmentRegistrationBinding.tiePasswordUser.text!!.clear()
        fragmentRegistrationBinding.tieRepeatPasswordUser.text!!.clear()
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