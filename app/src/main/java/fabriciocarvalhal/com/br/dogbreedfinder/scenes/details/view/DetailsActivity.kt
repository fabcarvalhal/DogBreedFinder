package fabriciocarvalhal.com.br.dogbreedfinder.scenes.details.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import fabriciocarvalhal.com.br.dogbreedfinder.R
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.details.Details
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.details.presenter.DetailsPresenter
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.item_breed.*

class DetailsActivity : AppCompatActivity(), Details.View {

    private val presenter: DetailsPresenter by lazy {
        DetailsPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        presenter.getBreed(intent.getIntExtra(BREED_ID, 0))
    }


    override fun displayBreed(breed: BreedModel) {
        if (!breed.url.isNullOrBlank()) {
            Glide.with(this)
                .load(breed.url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(bigImageView)
        } else {
            bigImageView?.visibility = View.GONE
        }
        dogNameTxtView?.text = getString(R.string.label_name_detail, breed.name)
        bredForTxtView?.text = getString(R.string.label_bredfor_detail, breed.bred_for)
        originTxtView?.text = getString(R.string.label_origin_detail, breed.origin)
        breedGroupTxtView?.text = getString(R.string.label_breedgroup_detail, breed.breed_group)
        weightTxtView?.text = getString(R.string.label_weight_detail, breed.weight)
        heightTxtView?.text = getString(R.string.label_height_detail, breed.height)
        temperamentTxtView?.text = getString(R.string.label_temperament_detail, breed.temperament)
        lifeSpanDetailTxtView?.text = getString(R.string.label_lifeSpan_detail, breed.life_span)

    }

    companion object {
        const val BREED_ID = "BREED_ID"
    }
}
