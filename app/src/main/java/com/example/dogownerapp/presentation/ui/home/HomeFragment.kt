package com.example.dogownerapp.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.databinding.FragmentHomeBinding
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var fauth = FirebaseAuth.getInstance()
        var f = FirebaseAuthDataSource(fauth)

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.button2.setOnClickListener {
            f.logout()
            Toast.makeText(requireContext(), fauth.currentUser.toString(), Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, AuthActivity::class.java))
            activity?.finish()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}