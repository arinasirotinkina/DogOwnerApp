package com.example.dogownerapp.presentation.ui.health

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogownerapp.databinding.FragmentHealthBinding
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Gender
import com.example.dogownerapp.presentation.adapter.DogAdapter
import java.util.Date

class HealthFragment : Fragment() {
    private var _binding: FragmentHealthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HealthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = DogAdapter()
        binding.dogsRecView.layoutManager = LinearLayoutManager(requireContext())
        binding.dogsRecView.adapter = adapter

        viewModel.dogs.observe(viewLifecycleOwner, Observer { dogs ->
            adapter.submitList(dogs)
        })

        binding.addDogButton.setOnClickListener {
            val newDog = Dog(
                name = "New Dog",
                breed = "Unknown",
                birthDate = Date(),
                gender = Gender.MALE,
                weight = 10.0,
                castration = false,
                sterilization = false,
                vaccinations = arrayListOf(),
                treatments = arrayListOf(),
            )
            viewModel.addDog(newDog)
            Log.i("DOG", "added")

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
