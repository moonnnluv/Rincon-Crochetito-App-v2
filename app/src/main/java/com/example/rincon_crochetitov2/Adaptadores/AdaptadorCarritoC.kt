package com.example.rincon_crochetitov2.Adaptadores

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rincon_crochetitov2.Modelos.ModeloProductoCarrito
import com.example.rincon_crochetitov2.databinding.ItemCarritoCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdaptadorCarritoC(
    private val context: Context,
    private val productos: MutableList<ModeloProductoCarrito>
) : RecyclerView.Adapter<AdaptadorCarritoC.HolderCarrito>() {

    inner class HolderCarrito(val b: ItemCarritoCBinding) :
        RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCarrito {
        val binding = ItemCarritoCBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return HolderCarrito(binding)
    }

    override fun getItemCount(): Int = productos.size

    override fun onBindViewHolder(holder: HolderCarrito, position: Int) {
        val item = productos[position]
        val b = holder.b

        // Nombre
        b.nombrePCar.text = item.nombre.ifBlank { "Producto sin nombre" }

        // Precio unitario (si hay descuento se usa precioDesc)
        val precioUnitario = obtenerPrecioUnitario(item)
        val totalItem = precioUnitario * item.cantidad
        item.precioFinal = totalItem.toString()

        // Textos de precios
        if (item.precioDesc.isNotBlank()) {
            // Tiene descuento
            b.precioOriginalPCar.visibility = View.VISIBLE
            b.precioOriginalPCar.text = "${item.precio} CLP"
            b.precioOriginalPCar.paintFlags =
                b.precioOriginalPCar.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            b.precioFinalPCar.text =
                "Total: ${totalItem} CLP   Unitario: ${item.precioDesc} CLP"
        } else {
            // Sin descuento
            b.precioOriginalPCar.visibility = View.GONE
            b.precioFinalPCar.text =
                "Total: ${totalItem} CLP   Unitario: ${item.precio} CLP"
        }

        // Cantidad
        b.cantidadPCar.text = item.cantidad.toString()

        // Botones +/- y eliminar
        b.btnAumentar.setOnClickListener {
            aumentarCantidad(item, holder.bindingAdapterPosition)
        }

        b.btnDisminuir.setOnClickListener {
            disminuirCantidad(item, holder.bindingAdapterPosition)
        }

        b.btnEliminar.setOnClickListener {
            eliminarProductoCarrito(item, holder.bindingAdapterPosition)
        }
    }

    private fun obtenerPrecioUnitario(item: ModeloProductoCarrito): Int {
        val base = if (item.precioDesc.isNotBlank()) item.precioDesc else item.precio
        return base.toIntOrNull() ?: 0
    }

    private fun aumentarCantidad(item: ModeloProductoCarrito, adapterPos: Int) {
        if (adapterPos == RecyclerView.NO_POSITION) return
        item.cantidad += 1
        actualizarCantidadEnFirebase(item, adapterPos)
    }

    private fun disminuirCantidad(item: ModeloProductoCarrito, adapterPos: Int) {
        if (adapterPos == RecyclerView.NO_POSITION) return

        if (item.cantidad <= 1) {
            // Si llega a 0, lo eliminamos del carrito
            eliminarProductoCarrito(item, adapterPos)
            return
        }

        item.cantidad -= 1
        actualizarCantidadEnFirebase(item, adapterPos)
    }

    private fun actualizarCantidadEnFirebase(
        item: ModeloProductoCarrito,
        adapterPos: Int
    ) {
        val uid = FirebaseAuth.getInstance().uid ?: return

        val ref = FirebaseDatabase.getInstance()
            .getReference("Usuarios")
            .child(uid)
            .child("CarritoCompras")
            .child(item.idProducto)

        val totalItem = obtenerPrecioUnitario(item) * item.cantidad
        item.precioFinal = totalItem.toString()

        val updates = mapOf(
            "cantidad" to item.cantidad,
            "precioFinal" to item.precioFinal
        )

        ref.updateChildren(updates)
            .addOnSuccessListener {
                if (adapterPos != RecyclerView.NO_POSITION &&
                    adapterPos < productos.size
                ) {
                    notifyItemChanged(adapterPos)
                }
                Toast.makeText(
                    context,
                    "Se actualizó la cantidad",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error al actualizar: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun eliminarProductoCarrito(
        item: ModeloProductoCarrito,
        adapterPos: Int
    ) {
        val uid = FirebaseAuth.getInstance().uid ?: return

        val ref = FirebaseDatabase.getInstance()
            .getReference("Usuarios")
            .child(uid)
            .child("CarritoCompras")
            .child(item.idProducto)

        ref.removeValue()
            .addOnSuccessListener {
                if (adapterPos != RecyclerView.NO_POSITION &&
                    adapterPos < productos.size
                ) {
                    productos.removeAt(adapterPos)
                    notifyItemRemoved(adapterPos)
                }
                Toast.makeText(
                    context,
                    "Producto eliminado del carrito",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error al eliminar: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
