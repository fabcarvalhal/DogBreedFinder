package fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.presenter

import android.util.Log
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.BreedList
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel
import fabriciocarvalhal.com.br.dogbreedfinder.services.DogsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class BreedListPresenter(private val view: BreedList.View) : BreedList.Presenter {
    private var offset = 0
    private var isLoading = false

    override fun getBreeds() {
        if (isLoading) {
            return
        }
        isLoading = true
        val apiCall = DogsService().getService().handleGetBreeds(DEFAULT_LIMIT, offset)
        offset =+ DEFAULT_LIMIT
        apiCall.enqueue(object: Callback<List<BreedModel>> {
            override fun onFailure(call: Call<List<BreedModel>>, t: Throwable) {
//                view.displayFailure(R.string.error_get_timezones)
                Log.d("ERRO", t.localizedMessage)
                isLoading = false
            }

            override fun onResponse(call: Call<List<BreedModel>>, response: Response<List<BreedModel>>) {
                isLoading = false
                response.body()?.let {
                    view.displayBreeds(it)
                } ?: run {
//                    view.displayFailure(R.string.error_get_timezones)
                }
            }
        })
    }

    companion object {
        const val DEFAULT_LIMIT = 5
    }


}