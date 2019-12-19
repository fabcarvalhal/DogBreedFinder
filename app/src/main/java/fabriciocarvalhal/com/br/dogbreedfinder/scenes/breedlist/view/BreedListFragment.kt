package fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.view


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fabriciocarvalhal.com.br.dogbreedfinder.R
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.BreedList
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.BreedListAdapter
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.model.BreedModel
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.breedlist.presenter.BreedListPresenter
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.details.view.DetailsActivity
import kotlinx.android.synthetic.main.fragment_breed_list.*
import kotlinx.android.synthetic.main.item_breed.*

/**
 * A simple [Fragment] subclass.
 */
class BreedListFragment : Fragment(), BreedList.View {

    private lateinit var adapter: BreedListAdapter

    private val presenter: BreedListPresenter by lazy {
        BreedListPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breed_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BreedListAdapter(arrayListOf()) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.BREED_ID, it)
            startActivity(intent)
        }



        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView?.adapter = adapter

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (adapter.itemCount  <= layoutManager.findLastCompletelyVisibleItemPosition() + 2) {
                    presenter.getBreeds()
                }

            }
        })

        presenter.getBreeds()
    }

    override fun displayBreeds(breeds: List<BreedModel>) {
        adapter.appendItems(breeds)
    }

}
