package nl.calips.autolinklibrary_app

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import nl.calips.autolinklibrary.*
import nl.calips.autolinklibrary.MODE_CUSTOM
import nl.calips.autolinklibrary.MODE_EMAIL
import nl.calips.autolinklibrary.MODE_HASHTAG
import nl.calips.autolinklibrary.MODE_MENTION
import nl.calips.autolinklibrary.MODE_PHONE
import nl.calips.autolinklibrary.MODE_URL
import nl.calips.autolinklibrary_app.databinding.ActivityRecyclerViewBinding
import nl.calips.autolinklibrary_app.databinding.RecyclerItemBinding


class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            private lateinit var viewHolderBinding: RecyclerItemBinding

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                viewHolderBinding = RecyclerItemBinding.inflate(layoutInflater, parent, false)
                val holderView = viewHolderBinding.root

                return object : RecyclerView.ViewHolder(holderView) {}
            }

            override fun getItemCount() = 200

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

                val autoLinkTextView = viewHolderBinding.autoLinkTextView
                val context = holder.itemView.context
                val custom = MODE_CUSTOM("\\sAndroid\\b")

                autoLinkTextView.addAutoLinkMode(
                        MODE_HASHTAG,
                        MODE_URL,
                        MODE_PHONE,
                        MODE_EMAIL,
                        custom,
                        MODE_MENTION
                )

                autoLinkTextView.addUrlTransformations(
                        "https://google.com" to "Google",
                        "https://en.wikipedia.org/wiki/Cyberpunk_2077" to "Cyberpunk",
                        "https://en.wikipedia.org/wiki/Fire_OS" to "FIRE",
                        "https://en.wikipedia.org/wiki/Wear_OS" to "Wear OS")

                autoLinkTextView.addSpan(MODE_URL, StyleSpan(Typeface.BOLD_ITALIC), UnderlineSpan())
                autoLinkTextView.addSpan(custom, StyleSpan(Typeface.BOLD))
                autoLinkTextView.addSpan(MODE_HASHTAG, BackgroundColorSpan(Color.GRAY), UnderlineSpan(), ForegroundColorSpan(Color.WHITE))

                autoLinkTextView.hashTagModeColor = ContextCompat.getColor(context, R.color.color2)
                autoLinkTextView.customModeColor = ContextCompat.getColor(context, R.color.color1)
                autoLinkTextView.mentionModeColor = ContextCompat.getColor(context, R.color.color3)
                autoLinkTextView.emailModeColor = ContextCompat.getColor(context, R.color.colorPrimary)
                autoLinkTextView.phoneModeColor = ContextCompat.getColor(context, R.color.colorAccent)

                val text = when {
                    position % 3 == 1 -> R.string.android_text_short
                    position % 3 == 2 -> R.string.android_text_short_second
                    else -> R.string.text_third
                }

                autoLinkTextView.text = getString(text)

                autoLinkTextView.onAutoLinkClick {
                    val message = if (it.originalText == it.transformedText) it.originalText
                    else "Original text - ${it.originalText} \n\nTransformed text - ${it.transformedText}"
                    val url = if (it.mode is MODE_URL) it.originalText else null
                    showDialog(it.mode.modeName, message, url)
                }
            }
        }
    }
}
