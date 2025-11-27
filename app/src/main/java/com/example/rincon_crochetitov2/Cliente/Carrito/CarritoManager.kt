package com.example.rincon_crochetitov2.Cliente.Carrito

import com.example.rincon_crochetitov2.Modelos.ModeloProductoCarrito

object CarritoManager {

    // Lista en memoria con el contenido del carrito
    private val items: MutableList<ModeloProductoCarrito> = mutableListOf()

    fun getItems(): List<ModeloProductoCarrito> = items

    // 👉 Lo usamos desde el Fragment para “sincronizar” lo que viene de Firebase
    fun setItems(lista: List<ModeloProductoCarrito>) {
        items.clear()
        // copiamos por si acaso para no depender de la misma referencia
        lista.forEach { origen ->
            items.add(
                ModeloProductoCarrito(
                    idProducto = origen.idProducto,
                    nombre = origen.nombre,
                    precio = origen.precio,
                    precioFinal = origen.precioFinal,
                    precioDesc = origen.precioDesc,
                    cantidad = origen.cantidad
                )
            )
        }
    }

    fun agregarProducto(item: ModeloProductoCarrito) {
        val existente = items.find { it.idProducto == item.idProducto }
        if (existente != null) {
            existente.cantidad += item.cantidad
            recalcularPrecioFinal(existente)
        } else {
            val nuevo = ModeloProductoCarrito(
                idProducto = item.idProducto,
                nombre = item.nombre,
                precio = item.precio,
                precioFinal = item.precioFinal,
                precioDesc = item.precioDesc,
                cantidad = if (item.cantidad <= 0) 1 else item.cantidad
            )
            recalcularPrecioFinal(nuevo)
            items.add(nuevo)
        }
    }

    fun actualizarCantidad(idProducto: String, nuevaCantidad: Int) {
        val item = items.find { it.idProducto == idProducto } ?: return
        item.cantidad = nuevaCantidad
        recalcularPrecioFinal(item)
    }

    fun eliminarProducto(idProducto: String) {
        items.removeAll { it.idProducto == idProducto }
    }

    fun limpiar() {
        items.clear()
    }

    fun obtenerTotal(): Double {
        return items.sumOf { item ->
            val precioFinalNum = item.precioFinal.toDoubleOrNull()
            if (precioFinalNum != null && precioFinalNum > 0.0) {
                precioFinalNum
            } else {
                val unit = item.precio.toDoubleOrNull() ?: 0.0
                val cant = if (item.cantidad > 0) item.cantidad else 1
                unit * cant
            }
        }
    }

    private fun recalcularPrecioFinal(item: ModeloProductoCarrito) {
        val unit = item.precio.toDoubleOrNull() ?: 0.0
        val cant = if (item.cantidad > 0) item.cantidad else 1
        item.precioFinal = (unit * cant).toString()
    }
}
