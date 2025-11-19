package com.example.rincon_crochetitov2.Administrador

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Constantes
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.databinding.ActivityRegistroAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegistroAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseDatabase: FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrarA.setOnClickListener {
            validarInformacion()
        }
    }

    private var nombres = ""
    private var email = ""
    private var contrasena = ""
    private var ccontrasena = ""
    private fun validarInformacion(){
        nombres = binding.etNombresA.text.toString().trim()
        email = binding.etEmailA.text.toString().trim()
        contrasena = binding.etPasswordA.text.toString().trim()
        ccontrasena = binding.etCPasswordA.text.toString().trim()

        if (nombres.isEmpty()){
            binding.etNombresA.error = "Ingrese nombres"
            binding.etNombresA.requestFocus()
        } else if (email.isEmpty()){
            binding.etEmailA.error = "Ingrese email"
            binding.etEmailA.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmailA.error = "Ingrese un email valido"
            binding.etEmailA.requestFocus()
        } else if (contrasena.isEmpty()){
            binding.etPasswordA.error = "Ingrese contraseña"
            binding.etPasswordA.requestFocus()
        } else if (contrasena.length <= 6){
            binding.etPasswordA.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etPasswordA.requestFocus()
        } else if (ccontrasena.isEmpty()){
            binding.etCPasswordA.error = "Confirme contraseña"
            binding.etCPasswordA.requestFocus()
        } else if (contrasena != ccontrasena){
            binding.etCPasswordA.error = "Las contraseñas no coinciden"
            binding.etCPasswordA.requestFocus()
        } else {
            registrarAdmin()
        }
    }

    private fun registrarAdmin() {
        progressDialog.setMessage("Creando cuenta...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, contrasena)
            .addOnSuccessListener {
                insertarInfoBD()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "No se pudo crear el usuario debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun insertarInfoBD(){
        progressDialog.setMessage("Guardando información...")

        val uiDB = firebaseAuth.uid
        val nombreDB = nombres
        val emailDB = email
        val tipousuario = "administrador"

        val tiempoDB = Constantes().obtenerTiempoD()

        val datosAdmin = HashMap<String, Any>()

        datosAdmin["uid"] = "$uiDB"
        datosAdmin["nombre"] = "$nombreDB"
        datosAdmin["email"] = "$emailDB"
        datosAdmin["tipoUsuario"] = "administrador"
        datosAdmin["tiempo_registro"] = "$tiempoDB"

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uiDB!!)
            .setValue(datosAdmin)
            .addOnSuccessListener{
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityAdmin::class.java))
                finish()
            }
            .addOnFailureListener{ e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se pudo guardar la información en la base de datos debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }
}