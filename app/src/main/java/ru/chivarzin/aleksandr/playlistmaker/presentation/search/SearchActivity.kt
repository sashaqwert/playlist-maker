package ru.chivarzin.aleksandr.playlistmaker.presentation.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.chivarzin.aleksandr.playlistmaker.Creator
import ru.chivarzin.aleksandr.playlistmaker.R
import ru.chivarzin.aleksandr.playlistmaker.domain.api.TracksInteractor
import ru.chivarzin.aleksandr.playlistmaker.domain.models.Track
import ru.chivarzin.aleksandr.playlistmaker.isDarkTheme

class SearchActivity : AppCompatActivity() {

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

    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val action_back = findViewById<ImageView>(R.id.search_action_back)
        action_back.setOnClickListener {
            finish()
        }

        you_searched = findViewById<TextView>(R.id.you_searched)
        clear_history = findViewById<Button>(R.id.clear_history)
        search = findViewById<EditText>(R.id.search)
        search_result = findViewById<RecyclerView>(R.id.search_result)
        search_result_sw = findViewById<ScrollView>(R.id.search_result_sw)
        search_pb = findViewById<ProgressBar>(R.id.search_pb)
        clear_history.setOnClickListener {
            Creator.provideSearchHistoryInteractor(this).clearHistory()
            you_searched.visibility = View.GONE
            search_result_sw.visibility = View.GONE
        }

        search.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && search_text == "") {
                if (!Creator.provideSearchHistoryInteractor(this).isEmpty()) {
                    showSearchHistory()
                }
            } else {
                if (search_text == "") {
                    you_searched.visibility = View.GONE
                    search_result_sw.visibility = View.GONE
                }
            }
        }

        icon_error = findViewById<ImageView>(R.id.icon_error)
        val clear_search = findViewById<ImageView>(R.id.clear_search)
        clear_search.setOnClickListener {
            search.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clear_search.visibility = View.GONE
                    search_result.visibility = View.INVISIBLE
                    if (!Creator.provideSearchHistoryInteractor(this@SearchActivity).isEmpty()) {
                        showSearchHistory()
                    }
                } else {
                    clear_search.visibility = View.VISIBLE
                    you_searched.visibility = View.GONE
                    clear_history.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                search_text = s.toString()
                searchDebounce()
            }
        }
        search.addTextChangedListener(simpleTextWatcher)
        error_text = findViewById<TextView>(R.id.error_text)
        refresh_search = findViewById<Button>(R.id.refresh_search)
        refresh_search.setOnClickListener {
            do_search()
        }
        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                if (search_text.isNotEmpty()) {
                    do_search()
                }
                true
            }
            false
        }
        if (!Creator.provideSearchHistoryInteractor(this).isEmpty()) {
            showSearchHistory()
        }
    }

    fun do_search() {
        if (search_text.isEmpty()) {
            return
        }
        show_loading()
        val consumer = object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>?) {
                runOnUiThread {
                    search_pb.visibility = View.GONE
                    if (foundTracks != null) {
                        if (foundTracks.isNotEmpty()) {
                            show_content(foundTracks)
                        } else {
                            show_empty()
                        }
                    } else {
                        show_error()
                    }
                }
            }

        }

        Creator.provideTracksInteractor().findMusic(search_text, consumer)
    }

    fun show_content(tracks: List<Track>) {
        val adapter = TrackAdapter(ArrayList(tracks), object : OnItemClickCallback {
            override fun callback(track: Track) {
                Creator.provideSearchHistoryInteractor(this@SearchActivity).addToHistory(track)
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

        if (isDarkTheme(this@SearchActivity)) {
            Glide.with(this@SearchActivity)
                .load(R.drawable.not_found_dark)
                .fitCenter()
                .into(icon_error)
        } else {
            Glide.with(this@SearchActivity)
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
        if (isDarkTheme(this)) {
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

    fun showSearchHistory() {
        val adapter = TrackAdapter(ArrayList<Track>(Creator.provideSearchHistoryInteractor(this).getHistory()), object : OnItemClickCallback {
            override fun callback(track: Track) {
                Creator.provideSearchHistoryInteractor(this@SearchActivity).addToHistory(track)
                showSearchHistory()
            }
        })
        clear_history.visibility = View.VISIBLE
        you_searched.visibility = View.VISIBLE
        search_result.visibility = View.VISIBLE
        search_result_sw.visibility = View.VISIBLE
        search_result.adapter = adapter
    }

    private val searchRunnable = Runnable { do_search() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString("search_text", search_text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        search_text = savedInstanceState?.getString("search_text", "") ?: ""
        search.setText(search_text)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}