package com.example.laboratoriofit.data.dieta

import com.google.firebase.firestore.CollectionReference

class RefeicaoRepository(private val db: CollectionReference) {

    fun getRefeicoes(callback: (List<Dieta>) -> Unit){
        db.orderBy("horario", com.google.firebase.firestore.Query.Direction.ASCENDING).get().addOnSuccessListener { documents ->
            val refeicoes = mutableListOf<Dieta>()
            for(document in documents){
                var ref = Dieta(
                    id = document.getString("id")!!,
                    descricao = document.getString("descricao")!!,
                    horario = document.getDouble("horario")!!.toInt(),
                    checked = document.getBoolean("checked")!!
                )
                refeicoes.add(ref)
            }
            callback(refeicoes)
        }
    }

    fun getRefeicao(id: String, callback: (Dieta) -> Unit){
        db.document(id).get().addOnSuccessListener { document ->
            val ref = Dieta(
                id = document.getString("id")!!,
                descricao = document.getString("descricao")!!,
                horario = document.getDouble("horario")!!.toInt(),
                checked = document.getBoolean("checked")!!
            )
            callback(ref)
        }
    }

    fun insert(refeicao: Dieta){
        val newRefeicao = db.document().id
        val copia = refeicao.copy(id = newRefeicao)
        db.document(newRefeicao).set(copia)
    }

    fun delete(id: String){
        db.document(id).delete()
    }

    fun update(id: String, map: Map<String, Any>){
        db.document(id).update(map)
    }
}