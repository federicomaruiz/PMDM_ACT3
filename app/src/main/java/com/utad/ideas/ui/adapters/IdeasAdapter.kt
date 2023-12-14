package com.utad.ideas.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utad.ideas.databinding.ItemIdeaListBinding
import com.utad.ideas.room.model.Ideas

class IdeasAdapter :
    ListAdapter<Ideas, IdeasAdapter.IdeaListViewHolder>(IdeasListItemCallBack) {

    // Implemento funcion del adaptador tengo que inflar la vista (visible) va crear y actualizar la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIdeaListBinding.inflate(inflater,parent,false)
        return IdeaListViewHolder(binding)

    }
    // Implemento funcion del adaptador asigna a cada item a un dato en concreto
    override fun onBindViewHolder(holder: IdeaListViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.tvIdeaName.text = item.ideaName
    }

    // Creo la clase interna que va a tener el viewHolder que va  recibir el binding del item.xml (layout)
    // Devuelve el recyclerView binding (la raiz de la vista que se esta viendo)
    inner class IdeaListViewHolder(val binding: ItemIdeaListBinding) :
        RecyclerView.ViewHolder(binding.root)


    // Creo un objeto que extiende de diffutil , va a comparar la informacion de dentro de la lista, si son iguales o cambia el contenido
    // le paso el objeto a comparar <AQUI>, tengo que implementar los miembros
    object IdeasListItemCallBack : DiffUtil.ItemCallback<Ideas>() {

        // Funcion que comprueba si los elementos son iguales
        override fun areItemsTheSame(oldItem: Ideas, newItem: Ideas): Boolean {
            return oldItem.id == newItem.id
        }

        // Funcion que comprueba si el contenido de los elementos cambio
        override fun areContentsTheSame(oldItem: Ideas, newItem: Ideas): Boolean {
            return oldItem.ideaName == newItem.ideaName
        }
    }


}