package com.example.rincon_crochetitov2.Adaptadores

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rincon_crochetitov2.Administrador.ListaClientes.EditarClienteActivity
import com.example.rincon_crochetitov2.Modelos.ModeloUsuario
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.databinding.ItemClienteBinding
import com.google.firebase.database.FirebaseDatabase

class AdaptadorCliente(
    private val mContext: Context,
    private val usuarioArrayList: ArrayList<ModeloUsuario>,
    private val usuarioIdArrayList: ArrayList<String>
) : RecyclerView.Adapter<AdaptadorCliente.HolderUsuario>() {

    private lateinit var binding: ItemClienteBinding
    private val refUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderUsuario {
        binding = ItemClienteBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderUsuario(binding.root)
    }

    override fun getItemCount(): Int = usuarioArrayList.size

    override fun onBindViewHolder(holder: HolderUsuario, position: Int) {
        val modeloUsuario = usuarioArrayList[position]
        val idUsuario = usuarioIdArrayList[position]

        val imagenU = modeloUsuario.imagen
        val nombresU = modeloUsuario.nombres
        val emailU = modeloUsuario.email
        val dniU = modeloUsuario.dni
        val ubicacionU = modeloUsuario.direccion
        val telefonoU = modeloUsuario.telefono
        val proveedor = modeloUsuario.proveedor

        var estado = modeloUsuario.estado
        if (estado.isEmpty()) estado = "activo"
        val bloqueado = estado == "bloqueado"

        holder.nombres.text = nombresU
        holder.email.text = emailU
        holder.dni.text = dniU
        holder.ubicacion.text = ubicacionU
        holder.telefono.text = "Tel.: $telefonoU"

        val estadoTexto = if (bloqueado) " (BLOQUEADO)" else " (ACTIVO)"
        holder.proveedor.text = "Proveedor: $proveedor$estadoTexto"

        holder.btnEditar.text = "Editar"
        holder.btnBloquear.text = if (bloqueado) "Desbloquear" else "Bloquear"

        try {
            Glide.with(mContext)
                .load(imagenU)
                .placeholder(R.drawable.img_perfil)
                .into(holder.imagen)
        } catch (_: Exception) { }

        // 👉 EDITAR: pasa SIEMPRE "idUsuario" en el intent
        holder.btnEditar.setOnClickListener {
            val intent = Intent(mContext, EditarClienteActivity::class.java)
            intent.putExtra("idUsuario", idUsuario)
            mContext.startActivity(intent)
        }

        // BLOQUEAR / DESBLOQUEAR (puedes dejarlo igual que lo teníamos)
        holder.btnBloquear.setOnClickListener {
            val nuevoEstado = if (bloqueado) "activo" else "bloqueado"
            val mensaje = if (bloqueado)
                "¿Deseas DESBLOQUEAR a $nombresU?"
            else
                "¿Deseas BLOQUEAR a $nombresU?"

            AlertDialog.Builder(mContext)
                .setTitle("Cambiar estado de usuario")
                .setMessage(mensaje)
                .setPositiveButton("Sí") { _, _ ->
                    cambiarEstadoUsuario(idUsuario, position, nuevoEstado)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    inner class HolderUsuario(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagen = binding.imagenC
        var nombres = binding.nombresCPerfil
        var email = binding.emailCPerfil
        var dni = binding.dniCPerfil
        var ubicacion = binding.ubicacion
        var telefono = binding.telefonoCPerfil
        var proveedor = binding.proveedorCPerfil

        var btnEditar = binding.btnLlamar
        var btnBloquear = binding.btnSms
    }

    private fun cambiarEstadoUsuario(idUsuario: String, position: Int, nuevoEstado: String) {
        refUsuarios.child(idUsuario).child("estado").setValue(nuevoEstado)
            .addOnSuccessListener {
                usuarioArrayList[position].estado = nuevoEstado
                notifyItemChanged(position)
                val texto = if (nuevoEstado == "bloqueado") "Usuario bloqueado" else "Usuario desbloqueado"
                Toast.makeText(mContext, texto, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(mContext, "Error al cambiar estado", Toast.LENGTH_SHORT).show()
            }
    }
}
