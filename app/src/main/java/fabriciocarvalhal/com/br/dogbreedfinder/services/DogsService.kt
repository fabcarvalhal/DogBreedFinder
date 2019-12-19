package fabriciocarvalhal.com.br.dogbreedfinder.services

import fabriciocarvalhal.com.br.dogbreedfinder.repository.DogsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DogsService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dogbreedfinder.000webhostapp.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getService() = retrofit.create(DogsRepository::class.java)
}