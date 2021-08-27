package com.example.laboratoriofit.ui.ficha

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.laboratoriofit.R
import com.example.laboratoriofit.data.ficha.Treino
import com.example.laboratoriofit.data.ficha.TreinoRepository
import com.example.laboratoriofit.databinding.FragmentAddTreinoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AddTreinoFragment : Fragment() {

    private var _binding: FragmentAddTreinoBinding? = null
    private val binding get() = _binding!!

    private val database : CollectionReference = FirebaseFirestore.getInstance().collection("User").document(
        Firebase.auth.currentUser?.uid.toString()).collection("Ficha")
    private var treinoRepository: TreinoRepository = TreinoRepository(database)
    private var viewModel : FichaViewModel = FichaViewModel(treinoRepository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTreinoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val args = AddTreinoFragmentArgs.fromBundle (bundle!!)

        binding.fieldNomeTreino.setOnClickListener {
            selectedItem(view)
        }

        if(args.idTreino.isNotBlank()){
            binding.topAppBarAddTreino.title = args.appBarTitle
            binding.addTreinoButton.text = getString(R.string.atualizar)
            viewModel.retrieveItem(args.idTreino).observe(this.viewLifecycleOwner){selectedItem ->
                bind(selectedItem)
            }
        }else{
            binding.treinoSerieAtualInput.visibility = View.GONE
            binding.deleteTreinoButton.visibility = View.GONE
            binding.addTreinoButton.setOnClickListener{
                addNewTreino()
            }
        }
    }

    fun selectedItem(view: View){
        val langName = resources.getStringArray(R.array.exercicio)
        var selectedIndex = 0

        if(binding.fieldNomeTreino.text.toString().isNotBlank()){
            selectedIndex = langName.indexOf(binding.fieldNomeTreino.text.toString())
        }

        var selectItem = langName[selectedIndex]
        val langDialog = MaterialAlertDialogBuilder(view.context)
        langDialog.setTitle("Selecionar treino")

        langDialog.setSingleChoiceItems(langName,selectedIndex){ dialog, which ->
            selectedIndex = which
            selectItem = langName[which]
        }
        langDialog.setPositiveButton("OK"){
            dialog, which ->
            binding.fieldNomeTreino.setText(selectItem)
        }
        langDialog.setNeutralButton("Cancelar"){
            dialog, which ->
            dialog.dismiss()
        }
        langDialog.show()
    }

    private fun isEntryValid(): Boolean{
        return viewModel.isEntryValid(
            binding.fieldNomeTreino.text.toString(),
            binding.fieldSerieTreino.text.toString(),
            binding.fieldRepeticaoTreino.text.toString()
        )
    }

    private fun isEntryValidU(): Boolean{
        return viewModel.isEntryValid(
            binding.fieldNomeTreino.text.toString(),
            binding.fieldSerieTreino.text.toString(),
            binding.fieldRepeticaoTreino.text.toString(),
            binding.fieldSerieAtualInput.text.toString()
        )
    }

    private fun addNewTreino(){
        if(isEntryValid()){
            viewModel.addNewTreino(
                binding.fieldNomeTreino.text.toString(),
                binding.fieldSerieTreino.text.toString(),
                binding.fieldRepeticaoTreino.text.toString()
            )
            findNavController().popBackStack()
        }else{
            hideKeyboard()
            Snackbar.make(binding.root, "Preencha todos os campos com valores válidos!", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun bind(treino: Treino){
        binding.apply {
            fieldNomeTreino.setText(treino.nome)
            fieldSerieAtualInput.setText(treino.serieAtual.toString())
            fieldSerieTreino.setText(treino.serie.toString())
            fieldRepeticaoTreino.setText(treino.repeticao.toString())
        }

        binding.deleteTreinoButton.setOnClickListener {
            viewModel.deleteTreino(treino.id)
            findNavController().popBackStack()
        }
        binding.addTreinoButton.setOnClickListener {
            hideKeyboard()
            if(isEntryValidU()){
                val docData = hashMapOf(
                    "nome" to binding.fieldNomeTreino.text.toString(),
                    "repeticao" to binding.fieldRepeticaoTreino.text.toString().toInt(),
                    "serie" to binding.fieldSerieTreino.text.toString().toInt(),
                    "serieAtual" to binding.fieldSerieAtualInput.text.toString().toInt()
                )
                viewModel.updateTreino(treino.id, docData)
                findNavController().popBackStack()
            }else{
                Snackbar.make(binding.root, "Preencha todos os campos com valores válidos!", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
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