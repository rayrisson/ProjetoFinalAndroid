package com.example.laboratoriofit

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.laboratoriofit.databinding.FragmentLoginBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!
    private lateinit var callbackManager: CallbackManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton: Button = binding.loginButton
        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create();

        loginButton.setOnClickListener{
            val email = binding.fieldEmail.text.toString()
            val password = binding.fieldPass.text.toString()
            signIn(email, password)
        }
        callbackManager = CallbackManager.Factory.create()
        binding.facebookButton.setPermissions("email", "public_profile")
        binding.facebookButton.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                Snackbar.make(binding.root, "Erro ao logar-se!", Snackbar.LENGTH_SHORT)
                    .show()
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                Snackbar.make(binding.root, "Erro ao logar-se!", Snackbar.LENGTH_SHORT)
                    .show()
            }
        })

        binding.cadastrarUser.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegistrarFragment()
            findNavController().navigate(action)
        }



    }

    private fun signIn(email: String, password: String){
        if(!validateForm()){
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Snackbar.make(binding.root, "Erro ao logar-se!", Snackbar.LENGTH_SHORT)
                        .show()
                }

            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.fieldEmail.error = "Required."
            valid = false
        } else {
            binding.fieldEmail.error = null
        }

        val password = binding.fieldPass.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.fieldPass.error = "Required."
            valid = false
        } else {
            binding.fieldPass.error = null
        }

        return valid
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Snackbar.make(binding.root, "Logou!", Snackbar.LENGTH_SHORT)
                        .show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}