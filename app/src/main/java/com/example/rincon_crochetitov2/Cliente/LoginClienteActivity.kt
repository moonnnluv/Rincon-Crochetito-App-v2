package com.example.rincon_crochetitov2.Cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.SeleccionarTipoActivity
import com.example.rincon_crochetitov2.databinding.ActivityLoginClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var refUsuarios: DatabaseReference

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        refUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios")

        progressDialog = ProgressDialog(this).apply {
            setTitle("Espere por favor")
            setCanceledOnTouchOutside(false)
        }

        binding.btnLoginC.setOnClickListener {
            validarInfo()
        }

        binding.btnLoginTel.setOnClickListener {
            startActivity(Intent(this, LoginTelActivity::class.java))
        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(this@LoginClienteActivity, RegistroClienteActivity::class.java))
        }

        binding.tvRecuperarPass.setOnClickListener {
            startActivity(Intent(this@LoginClienteActivity, RecuperarPasswordActivity::class.java))
        }
    }

    private fun validarInfo() {
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Email inválido"
            binding.etEmail.requestFocus()
        } else if (email.isEmpty()) {
            binding.etEmail.error = "Ingrese email"
            binding.etEmail.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese password"
            binding.etPassword.requestFocus()
        } else {
            loginCliente()
        }
    }

    private fun loginCliente() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()

                if (!task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "No se pudo iniciar sesión: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addOnCompleteListener
                }

                val uid = firebaseAuth.currentUser?.uid
                if (uid == null) {
                    Toast.makeText(this, "Error al obtener usuario", Toast.LENGTH_SHORT).show()
                    firebaseAuth.signOut()
                    return@addOnCompleteListener
                }

                validarClienteActivo(uid)
            }
    }

    private fun validarClienteActivo(uid: String) {
        progressDialog.setMessage("Validando usuario")
        progressDialog.show()

        refUsuarios.child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    if (!snapshot.exists()) {
                        Toast.makeText(
                            this@LoginClienteActivity,
                            "No se encontró el usuario en la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                        firebaseAuth.signOut()
                        return
                    }

                    val tipo = snapshot.child("tipoUsuario").value?.toString() ?: ""
                    val estado = snapshot.child("estado").value?.toString() ?: "activo"

                    if (tipo != "cliente") {
                        Toast.makeText(
                            this@LoginClienteActivity,
                            "Esta cuenta no es de cliente",
                            Toast.LENGTH_SHORT
                        ).show()
                        firebaseAuth.signOut()
                        return
                    }

                    if (estado == "bloqueado") {
                        Toast.makeText(
                            this@LoginClienteActivity,
                            "Tu usuario ha sido bloqueado por la administración",
                            Toast.LENGTH_LONG
                        ).show()
                        firebaseAuth.signOut()
                        return
                    }

                    // ✅ Cliente válido y activo: guardamos sesión y rol en SharedPreferences
                    val prefs = getSharedPreferences(
                        SeleccionarTipoActivity.PREFS_NAME,
                        MODE_PRIVATE
                    )
                    prefs.edit()
                        .putBoolean(SeleccionarTipoActivity.KEY_IS_LOGGED_IN, true)
                        .putString(SeleccionarTipoActivity.KEY_TIPO_USUARIO, "cliente")
                        .apply()

                    Toast.makeText(
                        this@LoginClienteActivity,
                        "Bienvenido(a)",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(
                            this@LoginClienteActivity,
                            MainActivityCliente::class.java
                        )
                    )
                    finishAffinity()
                }

                override fun onCancelled(error: DatabaseError) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@LoginClienteActivity,
                        "Error al validar usuario: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
