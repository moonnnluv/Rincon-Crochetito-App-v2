package com.example.rincon_crochetitov2.Administrador.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.databinding.FragmentInicioABinding

class FragmentInicioA : Fragment() {

    private var _binding: FragmentInicioABinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioABinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Listener de BottomNavigation: devuelve true cuando manejas el ítem
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.op_inicio_a -> {
                    replaceFragment(FragmentInicioA()) // o tu fragment de “inicio”
                    true
                }
                R.id.op_productos_a -> {
                    replaceFragment(FragmentProductosA())
                    true
                }
                R.id.op_usuarios_a -> {
                    replaceFragment(FragmentUsuariosA())
                    true
                }
                R.id.op_pagos_a -> {
                    replaceFragment(FragmentPagosA())
                    true
                }
                R.id.op_cerrar_sesion_a -> {
                    // TODO: limpiar SharedPreferences y navegar a LoginActivity
                    Toast.makeText(requireContext(), "Saliste de la aplicación", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        // Selección inicial
        binding.bottomNavigation.selectedItemId = R.id.op_productos_a

        // FAB
        binding.addFab.setOnClickListener {
            Toast.makeText(requireContext(), "Has presionado el botón flotante", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }


}
