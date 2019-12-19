package fabriciocarvalhal.com.br.dogbreedfinder.scenes.details.presenter

import android.util.Log
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.details.Details
import fabriciocarvalhal.com.br.dogbreedfinder.services.DogsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsPresenter(private val view: Details.View): Details.Presenter {

    override fun getBreed(id: Int) {
        val apiCall = DogsService().getService().handleGetBreed(id)
        apiCall.enqueue(object: Callback<BreedModel> {
            override fun onFailure(call: Call<BreedModel>, t: Throwable) {
//                view.displayFailure(R.string.error_get_timezones)
                Log.d("ERRO", t.localizedMessage)

            }

            override fun onResponse(call: Call<BreedModel>, response: Response<BreedModel>) {
                response.body()?.let {
                    view.displayBreed(it)
                    Log.d("DOGS", it.toString())
                } ?: run {
                    Log.d("Erro", "nil")
                }
            }
        })
    }

}