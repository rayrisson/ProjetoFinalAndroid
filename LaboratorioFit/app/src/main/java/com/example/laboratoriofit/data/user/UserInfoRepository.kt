package com.example.laboratoriofit.data.user

import com.google.firebase.firestore.DocumentReference

class UserInfoRepository(private val db: DocumentReference) {
    fun getInfo(callback: (UserInfo) -> Unit){
        db.get().addOnSuccessListener { document ->
            val info = UserInfo(
                nome = document.getString("nome")!!,
                altura = document.getDouble("altura")!!.toInt(),
                peso = document.getDouble("peso")!!,
                genero = document.getString("genero")!!
            )
            callback(info)
        }
    }

    fun update(map: Map<String, Any>){
        db.update(map)
    }
}