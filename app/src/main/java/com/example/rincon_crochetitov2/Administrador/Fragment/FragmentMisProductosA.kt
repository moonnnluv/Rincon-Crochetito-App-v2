package com.example.rincon_crochetitov2.Administrador.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rincon_crochetitov2.Adaptadores.AdaptadorCategoriaA
import com.example.rincon_crochetitov2.Modelos.ModeloCategoria
import com.example.rincon_crochetitov2.databinding.FragmentMisProductosABinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentMisProductosA : Fragment() {

    private lateinit var binding : FragmentMisProductosABinding
    private lateinit var mContext : Context



    private lateinit var categoriasArrayList : ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoriaV : AdaptadorCategoriaA

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMisProductosABinding.inflate(LayoutInflater.from(mContext), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //listarProductos()
        listarCategorias()
    }

    private fun listarCategorias() {
        categoriasArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriasArrayList.clear()
                for (ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriasArrayList.add(modelo!!)
                }
                adaptadorCategoriaV = AdaptadorCategoriaA(mContext, categoriasArrayList)
                binding.categoriasRV.adapter = adaptadorCategoriaV
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}