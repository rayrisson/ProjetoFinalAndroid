package com.example.laboratoriofit

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.laboratoriofit.databinding.FragmentRegistrarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegistrarFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegistrarBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance().collection("User")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.cadastrarUser.setOnClickListener{
            val email = binding.fieldEmail.text.toString()
            val password = binding.fieldPass.text.toString()
            val nome = binding.fieldNome.text.toString()
            val peso = binding.fieldPeso.text.toString()
            val altura = binding.fieldAltura.text.toString()
            val genero = binding.fieldGenero.text.toString()
            createAccount(email, password, nome, peso, altura, genero)
        }

        binding.fieldGenero.setOnClickListener{
            selectedItem(view)
        }
    }

    private fun createAccount(email: String, password: String, nome: String, peso: String, altura: String, genero: String) {
        if (validateForm()) {
            auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val data = hashMapOf(
                        "nome" to nome,
                        "genero" to genero,
                        "altura" to altura.toInt(),
                        "peso" to peso.toInt()
                    )
                    db.document(user?.uid.toString()).set(data)
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                }
            }
        }else{
            hideKeyboard()
            Snackbar.make(binding.root, "Preencha todos os campos de forma válida!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(): Boolean {
        val email = binding.fieldEmail.text.toString()
        val password = binding.fieldPass.text.toString()
        val nome = binding.fieldNome.text.toString()
        val peso = binding.fieldPeso.text.toString()
        val altura = binding.fieldAltura.text.toString()
        val genero = binding.fieldGenero.text.toString()

        return (email.length >= 0 && email.isNotBlank() && password.isNotBlank() && nome.isNotBlank() && peso.isNotBlank() && peso.toInt() > 0 && altura.isNotBlank() && altura.toInt() > 0 && genero.isNotBlank())
    }

    fun selectedItem(view: View){
        val langName = resources.getStringArray(R.array.genero)
        var selectedIndex = 0

        if(binding.fieldGenero.text.toString().isNotBlank()){
            selectedIndex = langName.indexOf(binding.fieldGenero.text.toString())
        }

        var selectItem = langName[selectedIndex]
        val langDialog = MaterialAlertDialogBuilder(view.context)
        langDialog.setTitle("Selecionar gênero")

        langDialog.setSingleChoiceItems(langName,selectedIndex){ dialog, which ->
            selectedIndex = which
            selectItem = langName[which]
        }
        langDialog.setPositiveButton("OK"){
                dialog, which ->
            binding.fieldGenero.setText(selectItem)
        }
        langDialog.setNeutralButton("Cancelar"){
                dialog, which ->
            dialog.dismiss()
        }
        langDialog.show()
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}