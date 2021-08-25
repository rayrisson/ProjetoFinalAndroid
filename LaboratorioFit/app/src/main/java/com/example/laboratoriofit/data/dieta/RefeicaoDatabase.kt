/*package com.example.laboratoriofit.data.dieta

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RefeicaoDatabase {
    val database: CollectionReference by lazy{ Firebase.firestore.collection("User").document(Firebase.auth.currentUser?.uid.toString()).collection("Dieta")}

    fun refeicaoRepository() = RefeicaoRepository(database)
}*/