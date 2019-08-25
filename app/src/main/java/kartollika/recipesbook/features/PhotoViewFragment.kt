package kartollika.recipesbook.features

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kartollika.recipesbook.R
import kotlinx.android.synthetic.main.photo_view_layout.*

class PhotoViewFragment : Fragment() {

//    private lateinit var image: ImageView
//    private lateinit var

    companion object {
        fun newInstance(uri: String, name: String) = PhotoViewFragment().apply {
            arguments = Bundle().apply {
                putString("photo_uri", uri)
                putString("name", name)
            }
        }
    }

//    override fun getLayoutRes(): Int = R.layout.photo_view_layout


//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return AlertDialog.Builder(context)
//            .setView(
//                LayoutInflater.from(context).inflate(R.layout.photo_view_layout, null, true).apply {
//                    image = this.photoViewImage
//                }
//            )
//            .create()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.photo_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = arguments?.getString("photo_uri")
        val name = arguments?.getString("name")

        toolbar.title = name
        toolbar.setNavigationOnClickListener {
            (activity as MainActivity).navigateUpFullScreen()
        }

        Glide.with(requireContext())
            .load(uri)
            .addListener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    target?.getSize { width, height ->
                        run {
                            val params = photoViewImage.layoutParams
                            params.height = height
                            photoViewImage.layoutParams = params
                        }
                    }
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(photoViewImage)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                activity?.onBackPressed()
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}