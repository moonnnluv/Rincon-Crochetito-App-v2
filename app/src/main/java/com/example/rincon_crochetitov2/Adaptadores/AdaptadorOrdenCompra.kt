package com.example.rincon_crochetitov2.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rincon_crochetitov2.Cliente.Orden.DetalleOrdenCActivity
import com.example.rincon_crochetitov2.Constantes
import com.example.rincon_crochetitov2.Modelos.ModeloOrdenCompra
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.databinding.ItemOrdenCompraBinding

class AdaptadorOrdenCompra(
    private val mContext: Context,
    var ordenesArrayList: ArrayList<ModeloOrdenCompra>
) : RecyclerView.Adapter<AdaptadorOrdenCompra.HolderOrdenCompra>() {

    private lateinit var binding: ItemOrdenCompraBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderOrdenCompra {
        binding = ItemOrdenCompraBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderOrdenCompra(binding.root)
    }

    override fun getItemCount(): Int {
        return ordenesArrayList.size
    }

    override fun onBindViewHolder(holder: HolderOrdenCompra, position: Int) {
        val ordenCompra = ordenesArrayList[position]

        val idOrden = ordenCompra.idOrden
        val tiempoOrden = ordenCompra.tiempoOrden
        val costo = ordenCompra.costo
        val estadoOrden = ordenCompra.estadoOrden

        holder.idOrdenItem.text = idOrden
        holder.costoOrdenItem.text = costo
        holder.estadoOrdenItem.text = estadoOrden

        // Colores según estado
        when (estadoOrden) {
            "Solicitud recibida" ->
                holder.estadoOrdenItem.setTextColor(
                    ContextCompat.getColor(mContext, R.color.azul_marino_oscuro)
                )

            "Pago Pendiente" ->
                holder.estadoOrdenItem.setTextColor(
                    ContextCompat.getColor(mContext, R.color.morado)
                )

            "En Preparación" ->
                holder.estadoOrdenItem.setTextColor(
                    ContextCompat.getColor(mContext, R.color.naranja)
                )

            "Entregado" ->
                holder.estadoOrdenItem.setTextColor(
                    ContextCompat.getColor(mContext, R.color.verde_oscuro2)
                )

            "Cancelado" ->
                holder.estadoOrdenItem.setTextColor(
                    ContextCompat.getColor(mContext, R.color.rojo)
                )
        }

        // 🔹 PARCHE IMPORTANTE: evitar NumberFormatException
        val fecha = tiempoOrden
            ?.toString()
            ?.toLongOrNull()   // si viene "", null o algo raro → null
            ?.let { millis -> Constantes().obtenerFecha(millis) }
            ?: "-"             // valor por defecto si no se puede convertir

        holder.fechaOrdenItem.text = fecha

        holder.ibSiguiente.setOnClickListener {
            val intent = Intent(mContext, DetalleOrdenCActivity::class.java)
            intent.putExtra("idOrden", idOrden)
            mContext.startActivity(intent)
        }
    }

    inner class HolderOrdenCompra(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var idOrdenItem = binding.idOrdenItem
        var fechaOrdenItem = binding.fechaOrdenItem
        var estadoOrdenItem = binding.estadoOrdenItem
        var costoOrdenItem = binding.costoOrdenItem
        var ibSiguiente = binding.ibSiguiente
    }
}
