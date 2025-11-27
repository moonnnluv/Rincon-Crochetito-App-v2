package com.example.rincon_crochetitov2.Cliente.Pago

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Cliente.Carrito.CarritoManager
import com.example.rincon_crochetitov2.databinding.ActivityPagoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Referencia a Realtime Database (ruta "Ordenes", puedes cambiar el nombre si quieres)
    private val ordenesRef: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Ordenes")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mostrar total del carrito
        val total = CarritoManager.obtenerTotal()
        binding.tvTotalPago.text = "Total: $${"%.0f".format(total)}"

        binding.btnConfirmarPago.setOnClickListener {
            registrarOrdenEnFirebase()
        }
    }

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

        val itemsCarrito = CarritoManager.getItems()
        if (itemsCarrito.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        // Lista de productos para guardar en la orden
        val productos = itemsCarrito.map { item ->
            mapOf(
                "idProducto" to item.idProducto,
                "nombre" to item.nombre,
                "cantidad" to item.cantidad,
                "precio" to item.precio,
                "precioFinal" to item.precioFinal
            )
        }

        val total = CarritoManager.obtenerTotal()

        val orden = hashMapOf(
            "userId" to usuario.uid,
            "productos" to productos,
            "total" to total,
            "direccionEnvio" to direccion,
            "comentario" to comentario,
            "estado" to "pendiente", // pago simulado
            "fechaCreacion" to System.currentTimeMillis()
        )

        binding.progressBarPago.visibility = View.VISIBLE
        binding.btnConfirmarPago.isEnabled = false

        // Guardamos la orden en Realtime Database
        ordenesRef.push()
            .setValue(orden)
            .addOnSuccessListener {
                CarritoManager.limpiar()
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
}
