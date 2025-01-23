package com.example.dogownerapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dogownerapp.databinding.DogListItemBinding
import com.example.dogownerapp.domain.model.Dog


class DogAdapter : ListAdapter<Dog, DogAdapter.DogViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = DogListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = getItem(position)
        holder.bind(dog)
    }

    class DogViewHolder(private val binding: DogListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dog: Dog) {
            binding.nameTextView.text = dog.name
            binding.breedTextView.text = dog.breed
            binding.weightTextView.text = "${dog.weight} kg"
            binding.ageTextView.text = dog.birthDate.toString()
            binding.SexTextView.text = dog.gender.toString()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Dog>() {
        override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem == newItem
        }
    }
}
