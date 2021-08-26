package com.example.laboratoriofit

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.laboratoriofit.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegistrarActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance().collection("User")
    private lateinit var binding: ActivityRegistrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.cadastrarUser.setOnClickListener{
            val email = binding.fieldEmail.text.toString()
            val password = binding.fieldPass.text.toString()
            val nome = binding.fieldNome.text.toString()
            val peso = binding.fieldPeso.text.toString()
            val altura = binding.fieldAltura.text.toString()
            createAccount(email, password, nome, peso, altura)
        }
    }

    private fun createAccount(email: String, password: String, nome: String, peso: String, altura: String) {
        if(!validateForm()){
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val data = hashMapOf(
                        "nome" to nome,
                        "genero" to "M",
                        "altura" to altura.toInt(),
                        "peso" to peso.toInt()
                    )
                    db.document(user?.uid.toString()).set(data)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                }

            }
    }

    private fun validateForm(): Boolean {
        val email = binding.fieldEmail.text.toString()
        val password = binding.fieldPass.text.toString()
        val nome = binding.fieldNome.text.toString()
        val peso = binding.fieldPeso.text.toString()
        val altura = binding.fieldAltura.text.toString()

        return (email.isNotBlank() && password.isNotBlank() && nome.isNotBlank() && peso.isNotBlank() && peso.toInt() > 0 && altura.isNotBlank() && altura.toInt() > 0)
    }
}