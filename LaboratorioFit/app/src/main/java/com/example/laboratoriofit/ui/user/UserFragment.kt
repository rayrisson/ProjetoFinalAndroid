package com.example.laboratoriofit.ui.user

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.laboratoriofit.data.dieta.Refeicao
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
        db.get().addOnSuccessListener { document ->
            if(document != null) {
                binding.WelcomeTitle.text = "Olá, ${document.getString("Nome")}"
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}