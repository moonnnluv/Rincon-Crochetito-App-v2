package com.example.rincon_crochetitov2.Cliente.Pago

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Modelos.ModeloProductoCarrito
import com.example.rincon_crochetitov2.databinding.ActivityPagoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private lateinit var usuariosRef: DatabaseReference
    private val ordenesRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Ordenes")
    }

    private val itemsCarrito = mutableListOf<ModeloProductoCarrito>()
    private var totalCarrito = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios")

        // 1) Cargamos productos del carrito y calculamos total
        cargarCarritoDesdeFirebase()

        // 2) Confirmar pago → crear orden
        binding.btnConfirmarPago.setOnClickListener {
            registrarOrdenEnFirebase()
        }
    }

    /** Lee "CarritoCompras" del usuario y calcula el total */
    private fun cargarCarritoDesdeFirebase() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.progressBarPago.visibility = View.VISIBLE

        usuariosRef.child(uid).child("CarritoCompras")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.progressBarPago.visibility = View.GONE

                    itemsCarrito.clear()
                    totalCarrito = 0.0

                    for (ds in snapshot.children) {
                        val modelo = ds.getValue(ModeloProductoCarrito::class.java)
                        if (modelo != null) {
                            itemsCarrito.add(modelo)
                            val totalItem = modelo.precioFinal.toDoubleOrNull() ?: 0.0
                            totalCarrito += totalItem
                        }
                    }

                    binding.tvTotalPago.text = "Total: ${totalCarrito.toInt()} CLP"

                    if (itemsCarrito.isEmpty()) {
                        Toast.makeText(
                            this@PagoActivity,
                            "Tu carrito está vacío",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.progressBarPago.visibility = View.GONE
                    Toast.makeText(
                        this@PagoActivity,
                        "Error al cargar el carrito: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    /** Crea la orden en Firebase usando los productos cargados */
    private fun registrarOrdenEnFirebase() {
        val usuario = auth.currentUser
        if (usuario == null) {
            Toast.makeText(
                this,
                "Debes iniciar sesión para finalizar la compra",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val direccion = binding.etDireccionEnvio.text.toString().trim()
        val comentario = binding.etComentarioPago.text.toString().trim()

        if (direccion.isEmpty()) {
            binding.etDireccionEnvio.error = "Ingresa una dirección de envío"
            return
        }

        if (itemsCarrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        // Estructura simple de productos para la orden
        val productos = itemsCarrito.map { item ->
            mapOf(
                "idProducto" to item.idProducto,
                "nombre" to item.nombre,
                "cantidad" to item.cantidad,
                "precio" to item.precio,
                "precioFinal" to item.precioFinal
            )
        }

        val orden = hashMapOf(
            "userId" to usuario.uid,
            "productos" to productos,
            "total" to totalCarrito,
            "direccionEnvio" to direccion,
            "comentario" to comentario,
            "estado" to "pendiente", // pago simulado
            "fechaCreacion" to System.currentTimeMillis()
        )

        binding.progressBarPago.visibility = View.VISIBLE
        binding.btnConfirmarPago.isEnabled = false

        ordenesRef.push()
            .setValue(orden)
            .addOnSuccessListener {
                // Limpia carrito en BD
                limpiarCarritoEnFirebase(usuario.uid)

                binding.progressBarPago.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Compra registrada correctamente. El pago es simulado y será gestionado por la administración.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBarPago.visibility = View.GONE
                binding.btnConfirmarPago.isEnabled = true
                Toast.makeText(
                    this,
                    "Error al registrar la orden: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    /** Borra el nodo CarritoCompras del usuario en Firebase */
    private fun limpiarCarritoEnFirebase(uid: String) {
        usuariosRef.child(uid).child("CarritoCompras").removeValue()
    }
}
