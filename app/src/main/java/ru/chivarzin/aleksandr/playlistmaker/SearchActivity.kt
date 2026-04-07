package ru.chivarzin.aleksandr.playlistmaker

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    private lateinit var search : EditText
    private var search_text = ""

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

        search = findViewById<EditText>(R.id.search)
        val search_result = findViewById<RecyclerView>(R.id.search_result)
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
                } else {
                    clear_search.visibility = View.VISIBLE
                    search_result.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
                search_text = s.toString()
0            }
        }
        search.addTextChangedListener(simpleTextWatcher)
        search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                true
            }
            false
        }

        //Фейк-результат поиска
        val t1 = Track(getString(R.string.t1_name), getString(R.string.t1_artist_name), getString(R.string.t1_duration), getString(R.string.t1_artwork))
        val t2 = Track(getString(R.string.t2_name), getString(R.string.t2_artist_name), getString(R.string.t2_duration), getString(R.string.t2_artwork))
        val t3 = Track(getString(R.string.t3_name), getString(R.string.t3_artist_name), getString(R.string.t3_duration), getString(R.string.t3_artwork))
        val t4 = Track(getString(R.string.t4_name), getString(R.string.t4_artist_name), getString(R.string.t4_duration), getString(R.string.t4_artwork))
        val t5 = Track(getString(R.string.t5_name), getString(R.string.t5_artist_name), getString(R.string.t5_duration), getString(R.string.t5_artwork))
        val searchResult = ArrayList<Track>()
        searchResult.add(t1)
        searchResult.add(t2)
        searchResult.add(t3)
        searchResult.add(t4)
        searchResult.add(t5)
        val trackAdapter = TrackAdapter(searchResult)
        search_result.adapter = trackAdapter

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
}