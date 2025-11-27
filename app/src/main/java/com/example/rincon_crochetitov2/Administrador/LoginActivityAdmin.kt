package com.example.rincon_crochetitov2.Administrador

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.SeleccionarTipoActivity
import com.example.rincon_crochetitov2.databinding.ActivityLoginAdminBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var email = ""
    private var contrasena = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginA.setOnClickListener {
            validarInfo()
        }

        binding.tvRegistrarA.setOnClickListener {
            startActivity(Intent(this, RegistroAdminActivity::class.java))
        }
    }

    private fun validarInfo() {
        email = binding.etEmailA.text.toString().trim()
        contrasena = binding.etPasswordA.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmailA.error = "Ingrese su email"
        } else if (contrasena.isEmpty()) {
            binding.etPasswordA.error = "Ingrese su contraseña"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailA.error = "Ingrese un email válido"
            binding.etEmailA.requestFocus()
        } else {
            loginAdmin()
        }
    }

    private fun loginAdmin() {
        progressDialog.setMessage("Iniciando sesión...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, contrasena)
            .addOnSuccessListener {
                progressDialog.dismiss()

                // 🔹 Guardar sesión y rol en SharedPreferences
                val prefs = getSharedPreferences(
                    SeleccionarTipoActivity.PREFS_NAME,
                    MODE_PRIVATE
                )
                prefs.edit()
                    .putBoolean(SeleccionarTipoActivity.KEY_IS_LOGGED_IN, true)
                    .putString(SeleccionarTipoActivity.KEY_TIPO_USUARIO, "admin")
                    .apply()

                // Ir al home de administrador
                startActivity(Intent(this, MainActivityAdmin::class.java))
                finishAffinity()

                Toast.makeText(
                    this,
                    "Bienvenido/a",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo iniciar sesión debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
