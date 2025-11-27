package com.example.rincon_crochetitov2.Modelos

class ModeloUsuario {

    var uid : String = ""
    var tipoUsuario : String = ""
    var email : String = ""
    var nombres : String = ""
    var dni : String = ""
    var proveedor : String = ""
    var telefono : String = ""
    var direccion : String = ""
    var imagen : String = ""

    // 👇 NUEVO CAMPO para bloqueo / desbloqueo
    // "" o "activo"  -> usuario activo
    // "bloqueado"    -> usuario bloqueado
    var estado: String = ""

    constructor()

    constructor(
        uid: String,
        tipoUsuario: String,
        email: String,
        nombres: String,
        dni: String,
        proveedor: String,
        telefono: String,
        direccion: String,
        imagen: String
    ) {
        this.uid = uid
        this.tipoUsuario = tipoUsuario
        this.email = email
        this.nombres = nombres
        this.dni = dni
        this.proveedor = proveedor
        this.telefono = telefono
        this.direccion = direccion
        this.imagen = imagen
        // estado lo puedes setear aparte si quieres:
        // this.estado = "activo"
    }
}
