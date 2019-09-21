package com.valerymiller.memorycards

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET

interface LoremPicsum {

    @GET("200/")
    fun randomImage(): Call<ResponseBody>

    companion object Factory {
        fun create(): LoremPicsum {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .build()

            return retrofit.create<LoremPicsum>(LoremPicsum::class.java)
        }
    }

}