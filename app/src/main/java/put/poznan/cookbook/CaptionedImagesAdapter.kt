package put.poznan.cookbook


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CaptionedImagesAdapter(
    private var captions: List<String>,
    private var imageUrls: List<String>
) : RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder>() {
    private lateinit var listener: Listener

    class ViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    interface Listener {
        fun onClick(position: Int)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cv = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_captioned_image, parent, false) as CardView
        return ViewHolder(cv)
    }

    override fun getItemCount(): Int {
        return captions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView
        val imageView = cardView.findViewById<ImageView>(R.id.info_image)

        val imageUrl = imageUrls[position]
        Glide.with(cardView.context)
            .load(imageUrl)
            .into(imageView)

//        val drawable = ContextCompat.getDrawable(cardView.context, imageIds[position])
//        imageView.setImageDrawable(drawable)
        imageView.contentDescription = captions[position]
        val textView = cardView.findViewById<TextView>(R.id.info_text)
        textView.text = captions[position]
        cardView.setOnClickListener {
            listener.onClick(position)
        }


    }
}