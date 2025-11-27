package com.example.rincon_crochetitov2.Cliente.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rincon_crochetitov2.Adaptadores.AdaptadorCarritoC
import com.example.rincon_crochetitov2.Cliente.Carrito.CarritoManager
import com.example.rincon_crochetitov2.Cliente.Pago.PagoActivity
import com.example.rincon_crochetitov2.Modelos.ModeloProductoCarrito
import com.example.rincon_crochetitov2.databinding.FragmentCarritoCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FragmentCarritoC : Fragment() {

    private lateinit var binding: FragmentCarritoCBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var adaptadorCarrito: AdaptadorCarritoC
    private val productosArrayList = ArrayList<ModeloProductoCarrito>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarritoCBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        configurarRecycler()
        cargarProductosCarrito()
        configurarBotonCrearOrden()

        return binding.root
    }

    private fun configurarRecycler() {
        adaptadorCarrito = AdaptadorCarritoC(
            requireContext(),
            productosArrayList
        )
        binding.carritoRv.adapter = adaptadorCarrito
    }

    private fun configurarBotonCrearOrden() {
        binding.btnCrearOrden.setOnClickListener {
            if (productosArrayList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Tu carrito está vacío",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Pasar a la pantalla de pago / creación de orden
                startActivity(Intent(requireContext(), PagoActivity::class.java))
            }
        }
    }

    private fun cargarProductosCarrito() {
        val uid = firebaseAuth.uid ?: return

        val ref = FirebaseDatabase.getInstance()
            .getReference("Usuarios")
            .child(uid)
            .child("CarritoCompras")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productosArrayList.clear()

                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloProductoCarrito::class.java)
                    if (modelo != null) {
                        productosArrayList.add(modelo)
                    }
                }

                // Sincronizamos con el manager (para PagoActivity)
                CarritoManager.setItems(productosArrayList)

                adaptadorCarrito.notifyDataSetChanged()
                actualizarTotal()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar el carrito: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun actualizarTotal() {
        val total = CarritoManager.obtenerTotal()
        binding.sumaProductos.text = "Total: ${"%.0f".format(total)} CLP"
    }
}
