package com.example.rincon_crochetitov2.data

import com.example.rincon_crochetitov2.Modelos.ResponseHttp
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("crear_preferencia")
    fun enviarOrdenDeCompra(@Body requestBody : RequestBody) : Call<ResponseHttp>

}