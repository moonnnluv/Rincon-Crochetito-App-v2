package com.example.rincon_crochetitov2.Cliente.Carrito

import com.example.rincon_crochetitov2.Modelos.ModeloProducto
import com.example.rincon_crochetitov2.Modelos.ModeloProductoCarrito

object CarritoManager {

    private val items = mutableListOf<ModeloProductoCarrito>()

    fun getItems(): List<ModeloProductoCarrito> = items

    fun agregarDesdeProducto(producto: ModeloProducto) {
        // Si ya existe en el carrito, sumamos cantidad
        val existente = items.find { it.idProducto == producto.id }
        if (existente != null) {
            existente.cantidad += 1
        } else {
            val precioBase = producto.precio.toDoubleOrNull() ?: 0.0
            val precioDesc = producto.precioDesc.toDoubleOrNull() ?: precioBase

            items.add(
                ModeloProductoCarrito(
                    idProducto = producto.id,
                    nombre = producto.nombre,
                    precio = precioBase.toString(),
                    precioFinal = precioDesc.toString(),
                    precioDesc = producto.precioDesc,
                    cantidad = 1
                )
            )
        }
    }

    fun agregarItem(item: ModeloProductoCarrito) {
        val existente = items.find { it.idProducto == item.idProducto }
        if (existente != null) {
            existente.cantidad += item.cantidad
        } else {
            items.add(item)
        }
    }

    fun actualizarCantidad(idProducto: String, nuevaCantidad: Int) {
        val item = items.find { it.idProducto == idProducto } ?: return
        if (nuevaCantidad <= 0) {
            items.remove(item)
        } else {
            item.cantidad = nuevaCantidad
        }
    }

    fun eliminar(idProducto: String) {
        items.removeAll { it.idProducto == idProducto }
    }

    fun limpiar() {
        items.clear()
    }

    fun obtenerTotal(): Double {
        return items.sumOf { item ->
            val precioFinal = item.precioFinal.toDoubleOrNull()
                ?: item.precio.toDoubleOrNull()
                ?: 0.0
            precioFinal * item.cantidad
        }
    }
}
