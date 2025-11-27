package com.example.rincon_crochetitov2.Administrador.ListaClientes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.databinding.ActivityEditarClienteBinding
import com.google.firebase.database.*

class EditarClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarClienteBinding
    private lateinit var refUsuarios: DatabaseReference
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        refUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios")

        // 👇 CLAVE: leer la MISMA key que mandamos desde el adaptador
        idUsuario = intent.getStringExtra("idUsuario")

        if (idUsuario.isNullOrEmpty()) {
            Toast.makeText(this, "Cliente no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarDatosUsuario()

        binding.btnGuardarCliente.setOnClickListener {
            guardarCambios()
        }
    }

    private fun cargarDatosUsuario() {
        refUsuarios.child(idUsuario!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        Toast.makeText(
                            this@EditarClienteActivity,
                            "Cliente no encontrado",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                        return
                    }

                    binding.etNombreCliente.setText(snapshot.child("nombres").value?.toString() ?: "")
                    binding.etTelefonoCliente.setText(snapshot.child("telefono").value?.toString() ?: "")
                    binding.etCorreoCliente.setText(snapshot.child("email").value?.toString() ?: "")
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@EditarClienteActivity,
                        "Error al cargar datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun guardarCambios() {
        val nombre = binding.etNombreCliente.text.toString().trim()
        val telefono = binding.etTelefonoCliente.text.toString().trim()
        val correo = binding.etCorreoCliente.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.etNombreCliente.error = "Ingresa un nombre"
            return
        }

        val updates = mapOf<String, Any?>(
            "nombres" to nombre,
            "telefono" to telefono,
            "email" to correo
        )

        refUsuarios.child(idUsuario!!)
            .updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
    }
}
