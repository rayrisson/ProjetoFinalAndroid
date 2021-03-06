package com.example.laboratoriofit.ui.dieta

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.laboratoriofit.R
import com.example.laboratoriofit.data.dieta.Dieta
import com.example.laboratoriofit.data.dieta.RefeicaoRepository
import com.example.laboratoriofit.databinding.FragmentAddRefeicaoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.ktx.Firebase

class AddRefeicaoFragment : Fragment() {
    private var _binding: FragmentAddRefeicaoBinding? = null
    private val binding get() = _binding!!
    private val database : CollectionReference = FirebaseFirestore.getInstance().collection("User").document(Firebase.auth.currentUser?.uid.toString()).collection("Dieta")
    private var refeicaoRepository: RefeicaoRepository = RefeicaoRepository(database)
    private var viewModelD : DietaViewModel = DietaViewModel(refeicaoRepository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRefeicaoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val args = AddRefeicaoFragmentArgs.fromBundle (bundle!!)

        binding.fieldHoraRefeicao.setOnClickListener{
            openTimePicker()
        }

        if(args.idDieta.isNotBlank()){
            binding.addRefeicaoButton.text = getString(R.string.atualizar)
            binding.topAppBarAddRef.title = args.title
            viewModelD.retrieveItem(args.idDieta).observe(this.viewLifecycleOwner){selectedItem ->
                //val ref = selectedItem
                bind(selectedItem)
            }
        }else{
            binding.deleteRefeicaoButton.visibility = View.GONE
            binding.addRefeicaoButton.setOnClickListener{
                addNewRefeicao()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openTimePicker(){
        val isSystem24hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_24H
        var textInput = binding.fieldHoraRefeicao.text.toString()
        var formattertime = textInput.split(":").toTypedArray()

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(if (textInput.isBlank()) 12 else formattertime[0].toInt())
            .setMinute(if (textInput.isBlank()) 0 else formattertime[1].toInt())
            .setTitleText("Defina o hor??rio")
            .build()

        picker.show(childFragmentManager, "TAG")

        picker.addOnPositiveButtonClickListener {
            val hora = picker.hour.toString().padStart(2, '0')
            val minuto = picker.minute.toString().padStart(2, '0')
            binding.fieldHoraRefeicao.setText("${hora}:${minuto}")
        }
    }

    private fun isEntryValid(): Boolean{
        return viewModelD.isEntryValid(
            binding.fieldDescRefeicao.text.toString(),
            binding.fieldHoraRefeicao.text.toString()
        )
    }

    private fun addNewRefeicao(){
        if(isEntryValid()){
            viewModelD.addNewRefeicao(
                binding.fieldDescRefeicao.text.toString(),
                binding.fieldHoraRefeicao.text.toString()
            )
            findNavController().popBackStack()
        }else{
            hideKeyboard()
            Snackbar.make(binding.root, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun bind(ref: Dieta){
        val m = ref.horario % 100
        val minuto = m.toString().padStart(2, '0')
        val h = ref.horario / 100
        val hora = h.toString().padStart(2, '0')
        binding.apply {
            fieldDescRefeicao.setText(ref.descricao)
            fieldHoraRefeicao.setText("${hora}:${minuto}")
        }

        binding.deleteRefeicaoButton.setOnClickListener {
            viewModelD.deleteRefeicao(ref.id)
            findNavController().popBackStack()
        }


        binding.addRefeicaoButton.setOnClickListener {
            if(isEntryValid()){
                val docData = hashMapOf(
                    "descricao" to binding.fieldDescRefeicao.text.toString(),
                    "horario" to binding.fieldHoraRefeicao.text.toString().replace(":", "").toInt()
                )
                viewModelD.updateRefeicao(ref.id, docData)
                findNavController().popBackStack()
            }else{
                hideKeyboard()
                Snackbar.make(binding.root, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
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