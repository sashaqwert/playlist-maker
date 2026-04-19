package ru.chivarzin.aleksandr.playlistmaker

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class TrackViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    val artwork = itemView.findViewById<ImageView>(R.id.artwork)
    val track_name = itemView.findViewById<TextView>(R.id.track_name)
    val artist_name = itemView.findViewById<TextView>(R.id.artist_name)
    val track_time = itemView.findViewById<TextView>(R.id.track_time)

    fun bind(model: Track) {
        if (model.trackName != null) {
            track_name.setText(model.trackName)
        } else {
            track_name.setText(R.string.track_name_error)
        }
        if (model.artistName != null) {
            artist_name.setText(model.artistName)
        } else {
            artist_name.setText(R.string.artist_name_error)
        }
        if (model.trackTimeMillis != null) {
            track_time.setText(SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis))
        } else {
            track_time.setText(R.string.track_time_default)
        }

        if (model.artworkUrl100 != null) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .fitCenter()
                .placeholder(R.drawable.artwork_default)
                .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
                .into(artwork)
        }
        else {
            Glide.with(itemView)
                .load(R.drawable.artwork_default)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(2.0f, itemView.context)))
                .into(artwork)
        }
        itemView.setOnClickListener {
            SearchHistory.add(model)
        }
    }

    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}