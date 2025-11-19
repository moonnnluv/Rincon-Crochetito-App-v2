package com.example.rincon_crochetitov2

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.rincon_crochetitov2.Cliente.MainActivityCliente
import com.example.rincon_crochetitov2.Administrador.MainActivityAdmin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.jvm.java

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun comprobarTipoUsuario(){
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, SeleccionarTipoActivity::class.java))
        }else{
            val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
            reference.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tipoU = snapshot.child("tipoUsuario").value

                        if (tipoU=="administrador"){
                            startActivity(Intent(this@SplashScreenActivity, MainActivityAdmin::class.java))
                            finishAffinity()
                        }else if (tipoU == "cliente"){
                            startActivity(Intent(this@SplashScreenActivity, MainActivityCliente::class.java))
                            finishAffinity()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                } )
        }
    }
}