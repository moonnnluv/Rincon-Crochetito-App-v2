package com.example.rincon_crochetitov2.Administrador.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rincon_crochetitov2.Administrador.Productos.AgregarProductoActivity
import com.example.rincon_crochetitov2.R
import com.example.rincon_crochetitov2.databinding.FragmentInicioABinding

class FragmentInicioA : Fragment() {

    private lateinit var binding : FragmentInicioABinding
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioABinding.inflate(inflater,container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_mis_productos_v->{
                    replaceFragment(FragmentMisProductosA())
                }
                R.id.op_mis_ordenes_v->{
                    replaceFragment(FragmentOrdenesA())
                }
            }
            true
        }


        replaceFragment(FragmentMisProductosA())
        binding.bottomNavigation.selectedItemId = R.id.op_mis_productos_v

        binding.addFab.setOnClickListener {
            val intent = Intent(mContext, AgregarProductoActivity::class.java)
            intent.putExtra("Edicion", false)
            mContext.startActivity(intent)
        }

        return binding.root


    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }

}
