package com.example.rincon_crochetitov2.Administrador.ListaClientes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Adaptadores.AdaptadorCliente
import com.example.rincon_crochetitov2.Modelos.ModeloUsuario
import com.example.rincon_crochetitov2.databinding.ActivityListaClientesBinding
import com.google.firebase.database.*

class ListaClientesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaClientesBinding

    // Lista original completa
    private lateinit var clientesArrayList: ArrayList<ModeloUsuario>
    private lateinit var clientesIdArrayList: ArrayList<String>

    // Lista filtrada que se muestra en pantalla
    private lateinit var clientesFiltrados: ArrayList<ModeloUsuario>
    private lateinit var clientesFiltradosIds: ArrayList<String>

    private lateinit var adaptadorCliente: AdaptadorCliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        listarClientes()

        // Escuchar cambios en el buscador
        binding.etBuscarCliente.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val texto = s?.toString() ?: ""
                aplicarFiltro(texto)
            }
        })
    }

    private fun listarClientes() {
        clientesArrayList = ArrayList()
        clientesIdArrayList = ArrayList()

        clientesFiltrados = ArrayList()
        clientesFiltradosIds = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.orderByChild("tipoUsuario").equalTo("cliente")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    clientesArrayList.clear()
                    clientesIdArrayList.clear()

                    for (ds in snapshot.children) {
                        val modeloCliente = ds.getValue(ModeloUsuario::class.java)
                        if (modeloCliente != null) {
                            clientesArrayList.add(modeloCliente)
                            clientesIdArrayList.add(ds.key ?: "")
                        }
                    }

                    // Al inicio, mostramos todos
                    aplicarFiltro(binding.etBuscarCliente.text?.toString() ?: "")
                }

                override fun onCancelled(error: DatabaseError) {
                    // podrías mostrar un Toast si quieres
                }
            })
    }

    private fun aplicarFiltro(texto: String) {
        val query = texto.lowercase()

        clientesFiltrados.clear()
        clientesFiltradosIds.clear()

        if (query.isEmpty()) {
            // Sin texto: muestra todo
            clientesFiltrados.addAll(clientesArrayList)
            clientesFiltradosIds.addAll(clientesIdArrayList)
        } else {
            for (i in clientesArrayList.indices) {
                val c = clientesArrayList[i]
                val id = clientesIdArrayList[i]

                val nombre = c.nombres.lowercase()
                val correo = c.email.lowercase()

                if (nombre.contains(query) || correo.contains(query)) {
                    clientesFiltrados.add(c)
                    clientesFiltradosIds.add(id)
                }
            }
        }

        // Si el adaptador aún no se creó, lo creamos, si no, solo notificamos cambios
        if (!::adaptadorCliente.isInitialized) {
            adaptadorCliente = AdaptadorCliente(
                this,
                clientesFiltrados,
                clientesFiltradosIds
            )
            binding.clienteRV.adapter = adaptadorCliente
        } else {
            adaptadorCliente.notifyDataSetChanged()
        }
    }
}
