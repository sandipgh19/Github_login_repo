package sandip.example.com.github_login_repo.binding

import android.databinding.BindingAdapter
import android.support.v4.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }

    @BindingAdapter(value = ["timeStamp", "format", "emptyTxt"], requireAll = true)
    fun bindDateTime(textView: TextView, timeStamp: String?, format: String, emptyTxt: String) {
        if (timeStamp != null) {
            val format1 = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US
            )
            format1.timeZone = TimeZone.getTimeZone("UTC")
            val mDate = format1.parse(timeStamp)
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            textView.text = dateFormat.format(mDate)
        } else
            textView.text = emptyTxt

    }
}
