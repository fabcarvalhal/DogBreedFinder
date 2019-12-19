package fabriciocarvalhal.com.br.dogbreedfinder.scenes.details

import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel

interface Details {
    interface View {
        fun displayBreed(breed: BreedModel)
    }

    interface Presenter {
        fun getBreed(id: Int)
    }

}