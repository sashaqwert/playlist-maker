package ru.chivarzin.aleksandr.playlistmaker.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.isDarkTheme
import ru.chivarzin.aleksandr.playlistmaker.presentation.models.TrackPresentation
import ru.chivarzin.aleksandr.playlistmaker.presentation.search.SearchState
import ru.chivarzin.aleksandr.playlistmaker.presentation.search.SearchViewModel
import ru.chivarzin.aleksandr.playlistmaker.ui.player.PlayerFragment

class SearchFragment : Fragment() {

    private lateinit var search : EditText
    private var search_text = ""
    private lateinit var search_result: RecyclerView
    private lateinit var search_result_sw: ScrollView
    private lateinit var error_text: TextView
    private lateinit var icon_error: ImageView
    private lateinit var refresh_search: Button
    private lateinit var clear_history: Button
    private lateinit var you_searched: TextView //Заголовок "Вы искали"
    private lateinit var search_pb: ProgressBar

    private val searchViewModel by viewModel<SearchViewModel>()
    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        you_searched = view.findViewById<TextView>(R.id.you_searched)
        clear_history = view.findViewById<Button>(R.id.clear_history)
        search = view.findViewById<EditText>(R.id.search)
        search_result = view.findViewById<RecyclerView>(R.id.search_result)
        search_result_sw = view.findViewById<ScrollView>(R.id.search_result_sw)
        search_pb = view.findViewById<ProgressBar>(R.id.search_pb)

        searchViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        clear_history.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }

        search.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && search_text == "") {
                searchViewModel.showSearchHistoryIfNotEmpty()
            } else {
                if (search_text == "") {
                    hideSearchHistory()
                }
            }
        }

        icon_error = view.findViewById<ImageView>(R.id.icon_error)
        val clear_search = view.findViewById<ImageView>(R.id.clear_search)
        clear_search.setOnClickListener {
            search.setText("")
            val inputMethodManager = activity?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clear_search.visibility = View.GONE
                    search_result.visibility = View.INVISIBLE
                    searchViewModel.showSearchHistoryIfNotEmpty()
                } else {
                    clear_search.visibility = View.VISIBLE
                    you_searched.visibility = View.GONE
                    clear_history.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                search_text = s.toString()
                searchViewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
        }
        search.addTextChangedListener(textWatcher)
        error_text = view.findViewById<TextView>(R.id.error_text)
        refresh_search = view.findViewById<Button>(R.id.refresh_search)
        refresh_search.setOnClickListener {
            searchViewModel.searchDebounce(
                changedText = search_text
            )
        }
        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (search_text.isNotEmpty()) {
                    searchViewModel.searchDebounce(
                        changedText = search_text
                    )
                }
                true
            }
            false
        }
        searchViewModel.showSearchHistoryIfNotEmpty()
    }

    fun hideSearchHistory() {
        you_searched.visibility = View.GONE
        search_result_sw.visibility = View.GONE
        search_pb.visibility = View.GONE
        icon_error.visibility = View.GONE
        error_text.visibility = View.GONE
        refresh_search.visibility = View.GONE
    }

    fun show_content(tracks: List<TrackPresentation>) {
        val adapter = TrackAdapter(ArrayList(tracks), object : OnItemClickCallback {
            override fun callback(track: TrackPresentation) {
                searchViewModel.addToHistory(track)
                findNavController().navigate(
                    R.id.action_searchFragment_to_playerFragment,
                    PlayerFragment.createArgs(track))
            }
        })
        search_result.adapter = adapter
        search_result.visibility = View.VISIBLE
        search_result_sw.visibility = View.VISIBLE
    }

    fun show_loading() {
        search_result_sw.visibility = View.GONE
        you_searched.visibility = View.GONE
        icon_error.visibility = View.GONE
        error_text.visibility = View.GONE
        refresh_search.visibility = View.GONE
        search_pb.visibility = View.VISIBLE
    }

    fun show_empty() {
        search_result_sw.visibility = View.GONE
        error_text.setText(R.string.search_not_found)
        icon_error.visibility = View.VISIBLE
        error_text.visibility = View.VISIBLE

        if (isDarkTheme(requireActivity())) {
            Glide.with(this)
                .load(R.drawable.not_found_dark)
                .fitCenter()
                .into(icon_error)
        } else {
            Glide.with(this)
                .load(R.drawable.not_found)
                .fitCenter()
                .into(icon_error)
        }
    }

    fun show_error() {
        search_pb.visibility = View.GONE
        search_result_sw.visibility = View.GONE
        error_text.setText(R.string.no_internet)
        icon_error.visibility = View.VISIBLE
        error_text.visibility = View.VISIBLE
        refresh_search.visibility = View.VISIBLE
        if (isDarkTheme(requireActivity())) {
            Glide.with(this)
                .load(R.drawable.no_internet_dark)
                .fitCenter()
                .into(icon_error)
        }
        else {
            Glide.with(this)
                .load(R.drawable.no_internet)
                .fitCenter()
                .into(icon_error)
        }
    }

    fun showSearchHistory(tracks: List<TrackPresentation>) {
        val adapter = TrackAdapter(ArrayList<TrackPresentation>(tracks), object :
            OnItemClickCallback {
            override fun callback(track: TrackPresentation) {
                searchViewModel.addToHistory(track)
                searchViewModel.showSearchHistoryIfNotEmpty()
                findNavController().navigate(
                    R.id.action_searchFragment_to_playerFragment,
                    PlayerFragment.createArgs(track))
            }
        })
        clear_history.visibility = View.VISIBLE
        you_searched.visibility = View.VISIBLE
        search_result.visibility = View.VISIBLE
        search_result_sw.visibility = View.VISIBLE
        search_result.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { search.removeTextChangedListener(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("search_text", search_text)
    }

    fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> show_loading()
            is SearchState.Content -> show_content(state.tracks)
            is SearchState.Empty -> show_empty()
            is SearchState.Error -> show_error()
            is SearchState.History -> showSearchHistory(state.tracks)
            is SearchState.emptyHistory -> hideSearchHistory()
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}