package fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist

import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel

interface BreedList {
    interface View {
        fun displayBreeds(breeds: List<BreedModel>)
    }

    interface Presenter {
        fun getBreeds()
    }
}