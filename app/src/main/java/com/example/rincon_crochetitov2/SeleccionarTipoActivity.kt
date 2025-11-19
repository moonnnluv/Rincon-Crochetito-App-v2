package com.example.rincon_crochetitov2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Cliente.LoginClienteActivity
import com.example.rincon_crochetitov2.Administrador.LoginActivityAdmin
import com.example.rincon_crochetitov2.databinding.ActivitySeleccionarTipoBinding

class SeleccionarTipoActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySeleccionarTipoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Iniciar sesión con administrador*/
        binding.tipoAdministrador.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginActivityAdmin::class.java))
        }

        /*Iniciar sesión con cliente*/
        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginClienteActivity::class.java))
        }
    }
}