package com.example.laboratoriofit.ui.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.laboratoriofit.CheckUser
import com.example.laboratoriofit.MainActivity
import com.example.laboratoriofit.R
import com.example.laboratoriofit.data.ficha.TreinoRepository
import com.example.laboratoriofit.data.user.UserInfoRepository
import com.example.laboratoriofit.databinding.FragmentAddTreinoBinding
import com.example.laboratoriofit.databinding.FragmentEditUserBinding
import com.example.laboratoriofit.ui.ficha.FichaViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class EditUserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentEditUserBinding? = null
    private val binding get() = _binding!!

    private val database : DocumentReference = FirebaseFirestore.getInstance().collection("User").document(
        Firebase.auth.currentUser?.uid.toString())
    private var userInfoRepository: UserInfoRepository = UserInfoRepository(database)
    private var viewModel : UserViewModel = UserViewModel(userInfoRepository)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.retrieveItem().observe(this.viewLifecycleOwner){selectedItem ->
            binding.apply {
                fieldNome.setText(selectedItem.nome)
                fieldAltura.setText(selectedItem.altura.toString())
                fieldPeso.setText(selectedItem.peso.toString())
                fieldGenero.setText(selectedItem.genero)
            }
        }

        binding.fieldGenero.setOnClickListener{
            selectedItem(view)
        }

        binding.editarUser.setOnClickListener {
            updateUser()
        }

        binding.deslogarUser.setOnClickListener {
            auth = Firebase.auth
            Signout()
        }
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

    fun isEntryValid(): Boolean{
        return viewModel.isEntryValid(
            binding.fieldNome.text.toString(),
            binding.fieldPeso.text.toString(),
            binding.fieldAltura.text.toString(),
            binding.fieldGenero.text.toString()
        )
    }

    fun updateUser(){
        if(isEntryValid()) {
            val docData = hashMapOf(
                "nome" to binding.fieldNome.text.toString(),
                "peso" to binding.fieldPeso.text.toString().toDouble(),
                "altura" to binding.fieldAltura.text.toString().toInt(),
                "genero" to binding.fieldGenero.text.toString()
            )
            viewModel.update(docData)
            findNavController().popBackStack()
        }else{
            hideKeyboard()
            Snackbar.make(binding.root, "Preencha todos os campos com valores válidos!", Snackbar.LENGTH_SHORT)
                .show()
        }

    }

    fun Signout(){
        auth.signOut()
        val intent = Intent(activity, CheckUser::class.java)
        startActivity(intent)
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