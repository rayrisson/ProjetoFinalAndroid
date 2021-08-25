package com.example.laboratoriofit.data.dieta

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.SnapshotMetadata
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.getField

class RefeicaoRepository(private val db: CollectionReference) {

    fun getRefeicoes(callback: (List<Refeicao>) -> Unit){
        db.orderBy("horario", com.google.firebase.firestore.Query.Direction.ASCENDING).get().addOnSuccessListener { documents ->
            val refeicoes = mutableListOf<Refeicao>()
            for(document in documents){
                var ref = Refeicao(
                    id = document.getString("id")!!,
                    descricao = document.getString("descricao")!!,
                    horario = document.getDouble("horario")!!.toInt()
                )
                refeicoes.add(ref)
            }
            callback(refeicoes)
        }
    }

    fun getRefeicao(id: String, callback: (Refeicao) -> Unit){
        db.document(id).get().addOnSuccessListener { document ->
            val ref = Refeicao(
                id = document.getString("id")!!,
                descricao = document.getString("descricao")!!,
                horario = document.getDouble("horario")!!.toInt()
            )
            callback(ref)
        }
        /*db.child(id).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val newRefeicao = snapshot.getValue(Refeicao::class.java)!!
                val oldListener = listenerTracker.put(snapshot.ref, this)
                if(oldListener != null){
                    db.child(id).removeEventListener(oldListener)
                }
                callback(newRefeicao)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })*/
    }

    fun insert(refeicao: Refeicao){
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



    /*fun removeAllListeners(){
        for(listener in listenerTracker.values){
            db.removeEventListener(listener)
        }
        listenerTracker.clear()
    }*/
}