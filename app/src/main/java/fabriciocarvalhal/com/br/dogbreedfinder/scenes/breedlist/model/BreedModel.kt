package fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model

data class BreedModel(
    val bred_for: String,
    val breed_group: String,
    val height: String,
    val id: String,
    val life_span: String,
    val name: String,
    val origin: String,
    val temperament: String,
    val url: String?,
    val weight: String
)