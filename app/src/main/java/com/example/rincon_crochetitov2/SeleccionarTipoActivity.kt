package com.example.rincon_crochetitov2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Administrador.LoginActivityAdmin
import com.example.rincon_crochetitov2.Administrador.MainActivityAdmin
import com.example.rincon_crochetitov2.Cliente.LoginClienteActivity
import com.example.rincon_crochetitov2.Cliente.MainActivityCliente
import com.example.rincon_crochetitov2.databinding.ActivitySeleccionarTipoBinding
import com.google.firebase.auth.FirebaseAuth

class SeleccionarTipoActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "user_prefs"
        const val KEY_TIPO_USUARIO = "tipoUsuario"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    private lateinit var binding: ActivitySeleccionarTipoBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 1) Revisar si ya hay sesión guardada
        checkSessionAndRedirectIfNeeded()

        // 2) Si no se redirigió, dejamos los botones activos
        binding.tipoAdministrador.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginActivityAdmin::class.java))
        }

        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginClienteActivity::class.java))
        }
    }

    private fun checkSessionAndRedirectIfNeeded() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val tipoUsuario = prefs.getString(KEY_TIPO_USUARIO, null)
        val currentUser = auth.currentUser

        // Si no hay usuario de Firebase o las prefs dicen que no hay sesión → no hacemos nada
        if (!isLoggedIn || currentUser == null || tipoUsuario.isNullOrEmpty()) {
            return
        }

        when (tipoUsuario) {
            "admin" -> {
                startActivity(Intent(this, MainActivityAdmin::class.java))
                finish()
            }
            "cliente" -> {
                startActivity(Intent(this, MainActivityCliente::class.java))
                finish()
            }
            else -> {
                // Valor raro → limpiamos y dejamos que el usuario elija
                prefs.edit().clear().apply()
            }
        }
    }
}
