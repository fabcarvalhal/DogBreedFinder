package fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
            lifeSpanTxtView?.text = list[position].life_span
            nameTxtView?.text = list[position].name
            Picasso.get()
                .load(list[position].url)
                .centerCrop()
                .resize(imageView?.width ?: 40, imageView?.height ?: 40)
                .into(imageView)
        }
    }

    fun appendItems(items: List<BreedModel>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    inner class BreedListViewHolder(view: View) : RecyclerView.ViewHolder(view)
}