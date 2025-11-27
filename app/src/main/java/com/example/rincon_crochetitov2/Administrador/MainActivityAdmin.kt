package com.example.rincon_crochetitov2.Administrador

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentCategoriasA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentInicioA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentMiTiendaA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentMisProductosA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentOrdenesA
import com.example.rincon_crochetitov2.Administrador.Fragment.FragmentProductosA
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.SeleccionarTipoActivity
import com.example.rincon_crochetitov2.databinding.ActivityMainAdminBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivityAdmin :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar (usa la Toolbar del layout incluido app_bar_main.xml)
        setSupportActionBar(binding.appBarMainAdmin.toolbar)

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

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
        binding.navigationView.setCheckedItem(R.id.op_inicio_v)
    }


    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))
        finish()
        Toast.makeText(applicationContext, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
    }

    private fun comprobarSesion(){
        /*Si el usuario no ha iniciado sesión, que lo diriga a OpcionesLogin*/
        if (firebaseAuth!!.currentUser==null){
            startActivity(Intent(applicationContext, SeleccionarTipoActivity::class.java))
        }else{
            Toast.makeText(applicationContext,"Usuario en línea", Toast.LENGTH_SHORT).show()
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.op_inicio_v->{
                replaceFragment(FragmentInicioA())
            }
            R.id.op_mi_tienda_v->{
                replaceFragment(FragmentMiTiendaA())
            }
            R.id.op_categorias_v->{
                replaceFragment(FragmentCategoriasA())
            }
            R.id.op_productos_v->{
                replaceFragment(FragmentProductosA())
            }
            R.id.op_cerrar_sesion_v->{
                cerrarSesion()
            }
            R.id.op_mis_productos_v->{
                replaceFragment(FragmentMisProductosA())
            }
            R.id.op_mis_ordenes_v->{
                replaceFragment(FragmentOrdenesA())
            }
        }
        binding.drawerLayoutAdmin.closeDrawer(GravityCompat.START)
        return true
    }
}