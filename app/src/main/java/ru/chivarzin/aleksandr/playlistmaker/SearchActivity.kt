package ru.chivarzin.aleksandr.playlistmaker

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    lateinit var sharedPrefs: SharedPreferences
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        SearchHistory.history = Gson().fromJson(sharedPrefs.getString(SEARCH_HISTORY, "[]"),
            object : TypeToken<List<Track>>() {}.type) // https://stackoverflow.com/a/51377183/7529334
        val action_back = findViewById<ImageView>(R.id.search_action_back)
        action_back.setOnClickListener {
            finish()
        }

        you_searched = findViewById<TextView>(R.id.you_searched)
        clear_history = findViewById<Button>(R.id.clear_history)
        search = findViewById<EditText>(R.id.search)
        search_result = findViewById<RecyclerView>(R.id.search_result)
        search_result_sw = findViewById<ScrollView>(R.id.search_result_sw)
        clear_history.setOnClickListener {
            SearchHistory.clear()
            you_searched.visibility = View.GONE
            search_result_sw.visibility = View.GONE
        }

        icon_error = findViewById<ImageView>(R.id.icon_error)
        val clear_search = findViewById<ImageView>(R.id.clear_search)
        clear_search.setOnClickListener {
            search.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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
                    if (!SearchHistory.isEmpty()) {
                        showSearchHistory()
                    }
                } else {
                    clear_search.visibility = View.VISIBLE
                    you_searched.visibility = View.GONE
                    clear_history.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
                search_text = s.toString()
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
        if (!SearchHistory.isEmpty()) {
            showSearchHistory()
        }
    }

    fun do_search() {
        iTunesService.findMusic(search_text).enqueue(object : Callback<SearchResult> {
            override fun onResponse(
                call: Call<SearchResult?>,
                response: Response<SearchResult?>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.results!!.isNotEmpty()) {
                        val searchResult = ArrayList<Track>(response.body()!!.results)
                        val trackAdapter = TrackAdapter(searchResult)
                        search_result.visibility = View.VISIBLE
                        search_result_sw.visibility = View.VISIBLE
                        search_result.adapter = trackAdapter
                    } else {
                        search_result_sw.visibility = View.GONE
                        error_text.setText(R.string.search_not_found)
                        icon_error.visibility = View.VISIBLE
                        error_text.visibility = View.VISIBLE
                        refresh_search.visibility = View.GONE
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
                } else {
                    show_error()
                }
            }

            override fun onFailure(call: Call<SearchResult?>, t: Throwable) {
                show_error()
            }
        })
    }

    fun show_error() {
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
        val adapter = TrackAdapter(SearchHistory.history, this)
        clear_history.visibility = View.VISIBLE
        you_searched.visibility = View.VISIBLE
        search_result.visibility = View.VISIBLE
        search_result.adapter = adapter
    }

    // Source - https://stackoverflow.com/a/57686965
    // Posted by Izadi Egizabal
    // Retrieved 2026-04-09, License - CC BY-SA 4.0
    fun isDarkTheme(activity: Activity): Boolean {
        return activity.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroy() {
        super.onDestroy()
        val history = Gson().toJson(SearchHistory.history)
        sharedPrefs.edit().putString(SEARCH_HISTORY, history).apply()
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
        const val SEARCH_HISTORY = "search_history"
    }
}