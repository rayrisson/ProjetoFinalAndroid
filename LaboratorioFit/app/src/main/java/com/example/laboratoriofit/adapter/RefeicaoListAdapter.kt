package com.example.laboratoriofit.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.laboratoriofit.R
import com.example.laboratoriofit.data.dieta.Dieta
import com.example.laboratoriofit.databinding.RefeicaoListBinding
import com.google.android.material.color.MaterialColors

class RefeicaoListAdapter(private val onRefeicaoClick: (Dieta) -> Unit, private val onRefeicaoLongClick: (Dieta) -> Unit) :
    ListAdapter<Dieta, RefeicaoListAdapter.RefeicaoViewHolder>(DiffCallback) {
        class RefeicaoViewHolder(private var binding: RefeicaoListBinding)
            : RecyclerView.ViewHolder(binding.root){
                fun bind(refeicao: Dieta){
                    binding.apply{
                        val refeicoes = refeicao.descricao.split(",").toTypedArray()
                        val m = refeicao.horario % 100
                        val minuto = m.toString().padStart(2, '0')
                        val h = refeicao.horario / 100
                        val hora = h.toString().padStart(2, '0')
                        titleHorario.text = "${hora}:${minuto}"

                        for(ref in refeicoes){
                            val textDesc = TextView(root.context)
                            textDesc.text = ref.trim()
                            if(refeicao.checked){
                                textDesc.setTextColor(Color.WHITE)
                            }
                            listRef.addView(textDesc)
                        }

                        if(refeicao.checked){
                            val teste =  MaterialColors.getColor(binding.root, R.attr.colorPrimaryVariant,binding.root.getResources().getColor(R.color.primaryDarkColor))
                            card.setCardBackgroundColor(teste)
                            titleHorario.setTextColor(Color.WHITE)
                        }
                    }
                }
            }

        companion object{
            private val DiffCallback = object: DiffUtil.ItemCallback<Dieta>(){
                override fun areItemsTheSame(oldItem: Dieta, newItem: Dieta): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: Dieta, newItem: Dieta): Boolean {
                    return oldItem.descricao == newItem.descricao
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefeicaoViewHolder {
        val lp = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        val bind = RefeicaoListBinding.inflate(LayoutInflater.from(parent.context))
        bind.root.layoutParams = lp
        return RefeicaoViewHolder(bind)
    }

    override fun onBindViewHolder(holder: RefeicaoViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onRefeicaoClick(current)
        }
        holder.itemView.setOnLongClickListener {
            onRefeicaoLongClick(current)
            true
        }
        holder.bind(current)
    }

}