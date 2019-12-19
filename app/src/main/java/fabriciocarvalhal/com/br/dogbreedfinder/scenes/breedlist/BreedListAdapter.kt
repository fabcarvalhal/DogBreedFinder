package fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import fabriciocarvalhal.com.br.dogbreedfinder.R
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel
import kotlinx.android.synthetic.main.item_breed.view.*

class BreedListAdapter(
    val list: ArrayList<BreedModel>,
    val onClick: ((Int) -> Unit)
) : RecyclerView.Adapter<BreedListAdapter.BreedListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedListViewHolder {
        return BreedListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_breed, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BreedListViewHolder, position: Int) {
        with(holder.itemView) {


            list[position].let {
                setOnClickListener { _ ->
                    onClick(it.id)
                }

                lifeSpanTxtView?.text = it.life_span
                nameTxtView?.text = it.name
                if (!it.url.isNullOrBlank()) {
                    Glide.with(this)
                        .load(it.url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView)
                } else {
                    imageView?.visibility = View.GONE
                }
            }

        }
    }

    fun appendItems(items: List<BreedModel>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    inner class BreedListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}