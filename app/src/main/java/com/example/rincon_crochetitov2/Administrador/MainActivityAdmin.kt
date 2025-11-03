package com.example.rincon_crochetitov2.Administrador

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentInicioA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentPagosA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentProductosA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentUsuariosA
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.databinding.ActivityMainAdminBinding
import com.google.android.material.navigation.NavigationView

class MainActivityAdmin :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar (usa la Toolbar del layout incluido app_bar_main.xml)
        setSupportActionBar(binding.appBarMainAdmin.toolbar)

        // Listener del menú lateral
        binding.navigationView.setNavigationItemSelectedListener(this)

        // Toggle hamburguesa <-> drawer
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutAdmin,
            binding.appBarMainAdmin.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayoutAdmin.addDrawerListener(toggle)
        toggle.syncState()

        // Fragment por defecto
        replaceFragment(FragmentInicioA())
        binding.navigationView.setCheckedItem(R.id.op_inicio_a)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.op_inicio_a     -> replaceFragment(FragmentInicioA())
            R.id.op_productos_a  -> replaceFragment(FragmentProductosA())
            R.id.op_usuarios_a   -> replaceFragment(FragmentUsuariosA())
            R.id.op_pagos_a      -> replaceFragment(FragmentPagosA())
            R.id.op_cerrar_sesion_a -> {
                // TODO: limpiar SharedPreferences y volver al LoginActivity
                Toast.makeText(this, "Saliste de la aplicación", Toast.LENGTH_SHORT).show()
            }
        }
        binding.drawerLayoutAdmin.closeDrawer(GravityCompat.START)
        return true
    }
}
