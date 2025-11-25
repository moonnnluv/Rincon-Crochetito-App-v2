package com.example.rincon_crochetitov2.Adaptadores

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rincon_crochetitov2.Administrador.Productos.ProductosCatAActivity
import com.example.rincon_crochetitov2.Modelos.ModeloCategoria
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.databinding.ItemCategoriaABinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AdaptadorCategoriaA(
    private val mContext: Context,
    private val categoriaArrayList: ArrayList<ModeloCategoria>
) : RecyclerView.Adapter<AdaptadorCategoriaA.HolderCategoriaA>() {

    private lateinit var binding: ItemCategoriaABinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoriaA {
        binding = ItemCategoriaABinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderCategoriaA(binding.root)
    }

    override fun getItemCount(): Int = categoriaArrayList.size

    override fun onBindViewHolder(holder: HolderCategoriaA, position: Int) {
        val modelo = categoriaArrayList[position]

        val id = modelo.id
        val categoria = modelo.categoria
        val imagen = modelo.imagenUrl

        holder.item_nombre_c_v.text = categoria

        Glide.with(mContext)
            .load(imagen)
            .placeholder(R.drawable.categorias)
            .into(holder.item_img_c_v)

        holder.item_act_categ_c_v.setOnClickListener {
            actualizarNomCat(id)
        }

        holder.item_eliminar_c.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setTitle("Eliminar categoria")
            builder.setMessage("¿Estás seguro(a) de eliminar esta categoría?")
                .setPositiveButton("Confirmar") { a, _ ->
                    eliminarCategoria(modelo)
                    a.dismiss()
                }
                .setNegativeButton("Cancelar") { a, _ ->
                    a.dismiss()
                }
            builder.show()
        }

        holder.item_ver_productos.setOnClickListener {
            val intent = Intent(mContext, ProductosCatAActivity::class.java)
            intent.putExtra("nombreCat", categoria)
            Toast.makeText(
                mContext,
                "Categoría seleccionada $categoria",
                Toast.LENGTH_SHORT
            ).show()
            mContext.startActivity(intent)
        }
    }

    private fun eliminarCategoria(modelo: ModeloCategoria) {
        val idCat = modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        ref.child(idCat).removeValue()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Categoría eliminada", Toast.LENGTH_SHORT).show()
                eliminarImgCat(idCat)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    mContext,
                    "No se eliminó la categoria debido a ${e.message} ",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun eliminarImgCat(idCat: String) {
        val nombreImg = idCat
        val rutaImagen = "Categorias/$nombreImg"
        val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageRef.delete()
            .addOnSuccessListener {
                Toast.makeText(
                    mContext,
                    "Se eliminó la imagen de la categoría",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "${e.message} ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarNomCat(id: String) {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.dialog_act_nom_cat)

        val etNuevoNomCat: EditText = dialog.findViewById(R.id.etNuevoNomCat)
        val btnActualizarNomCat: MaterialButton = dialog.findViewById(R.id.btnActualizarNomCat)
        val ibCerrar: ImageButton = dialog.findViewById(R.id.ibCerrar)

        btnActualizarNomCat.setOnClickListener {
            val nuevoNombre = etNuevoNomCat.text.toString().trim()
            if (nuevoNombre.isNotEmpty()) {
                actualizarNomCatBD(id, nuevoNombre)
                dialog.dismiss()
            } else {
                Toast.makeText(mContext, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
            }
        }

        ibCerrar.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun actualizarNomCatBD(idCat: String, nuevoNombre: String) {
        val hashMap = HashMap<String, Any>()
        hashMap["categoria"] = nuevoNombre

        val ref = FirebaseDatabase.getInstance().getReference("Categorias").child(idCat)
        ref.updateChildren(hashMap)
            .addOnSuccessListener {
                Toast.makeText(mContext, "Nombre actualizado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    mContext,
                    "Ha ocurrido un error debido a:  ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    inner class HolderCategoriaA(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_nombre_c_v = binding.itemNombreCV
        val item_act_categ_c_v = binding.itemActualizarCat
        val item_eliminar_c = binding.itemEliminarC
        val item_img_c_v = binding.imagenCategCV
        val item_ver_productos = binding.itemVerProductos
    }
}
