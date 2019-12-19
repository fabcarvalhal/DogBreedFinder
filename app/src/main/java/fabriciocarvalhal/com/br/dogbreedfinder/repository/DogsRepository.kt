package fabriciocarvalhal.com.br.dogbreedfinder.repository

import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsRepository {
    @GET("breeds/{limit}/{offset}")
    fun handleGetBreeds(@Path("limit") limit: Int, @Path("offset") offset: Int) : Call<List<BreedModel>>

    @GET("breed/{id}")
    fun handleGetTimezone(@Path("id") id: Int) : Call<BreedModel>
}