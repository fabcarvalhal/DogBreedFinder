package fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.presenter

import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.BreedList
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel
import fabriciocarvalhal.com.br.dogbreedfinder.services.DogsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedListPresenter(private val view: BreedList.View) : BreedList.Presenter {


    override fun getBreeds(limit: Int, offset: Int) {
        val apiCall = DogsService().getService().handleGetBreeds(limit, offset)
        apiCall.enqueue(object: Callback<List<BreedModel>> {
            override fun onFailure(call: Call<List<BreedModel>>, t: Throwable) {
//                view.displayFailure(R.string.error_get_timezones)
                print("DEU UM ERROOO")
            }

            override fun onResponse(call: Call<List<BreedModel>>, response: Response<List<BreedModel>>) {
                response.body()?.let {
                    view.displayBreeds(it)
                } ?: run {
//                    view.displayFailure(R.string.error_get_timezones)
                }
            }
        })
    }


}