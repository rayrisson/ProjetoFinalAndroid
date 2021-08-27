package com.example.laboratoriofit.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.laboratoriofit.R
import com.example.laboratoriofit.databinding.FragmentUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {

    //private lateinit var homeViewModel: HomeViewModel
    private val db = FirebaseFirestore.getInstance().collection("User").document(Firebase.auth.currentUser?.uid.toString())
    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.get().addOnSuccessListener { document ->
            if(document != null) {
                binding.WelcomeTitle.text = "Olá, ${document.getString("nome")!!.split(" ")[0]}"
                binding.alturaValue.text = "${document.getDouble("altura").toString()} cm"
                binding.pesoValue.text = "${document.getDouble("peso").toString()} kg"
                binding.imcValue.text =  ("%.2f".format(getIMC(document.getDouble("peso")!!, document.getDouble("altura")!!))).toString()
            }
        }

        binding.topAppBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.setting_user -> {
                    val action = UserFragmentDirections.actionNavigationUserToEditUserFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getIMC(peso: Double, altura: Double): Double{
        return peso / ((altura/100) * (altura/100))
    }
}