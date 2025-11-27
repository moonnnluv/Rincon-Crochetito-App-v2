package com.example.rincon_crochetitov2.Administrador.Productos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Adaptadores.AdaptadorProducto
import com.example.rincon_crochetitov2.Modelos.ModeloProducto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.rincon_crochetitov2.databinding.ActivityProductosCatAactivityBinding


class ProductosCatAActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductosCatAactivityBinding
    private var nombreCat = ""

    private lateinit var productoArrayList : ArrayList<ModeloProducto>
    private lateinit var adaptadorProductos : AdaptadorProducto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductosCatAactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nombreCat = intent.getStringExtra("nombreCat").toString()

        binding.txtProductoCat.text = "Categoria - ${nombreCat}"

        listarProductos(nombreCat)

    }

    private fun listarProductos(nombreCat : String) {
        productoArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.orderByChild("categoria").equalTo(nombreCat).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productoArrayList.clear()
                for (ds in snapshot.children){
                    val modeloProducto = ds.getValue(ModeloProducto::class.java)
                    productoArrayList.add(modeloProducto!!)
                }
                adaptadorProductos = AdaptadorProducto(this@ProductosCatAActivity, productoArrayList)
                binding.productosRV.adapter = adaptadorProductos
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}