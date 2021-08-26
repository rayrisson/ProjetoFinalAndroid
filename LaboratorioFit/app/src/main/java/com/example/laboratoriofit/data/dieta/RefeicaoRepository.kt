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
                    horario = document.getDouble("horario")!!.toInt()
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
                horario = document.getDouble("horario")!!.toInt()
            )
            callback(ref)
        }
    }

    fun insert(refeicao: Dieta){
        db.add(refeicao).addOnSuccessListener { document ->
            val id: String = document.id
            db.document(id).update("id", id)
        }
    }

    fun delete(id: String){
        db.document(id).delete()
    }

    fun update(id: String, map: Map<String, Any>){
        db.document(id).update(map)
    }
}