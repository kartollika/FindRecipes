package kartollika.recipesbook.common.ui


/*
class FlexibleChip(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr), Checkable {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    private val layoutRes = R.layout.chip_detail

    private lateinit var closeIcon: ImageView
    private lateinit var textView: TextView
    private var isChipChecked by Delegates.observable(false,
        { property, oldValue, newValue -> onCheckedChangeListener?.invoke(newValue) })
    private var onCheckedChangeListener: ((Boolean) -> Unit)? = null
    private var closeAction: (() -> Unit)? = null

    init {
        initView()
    }

    private fun initView() {
        val inflateView = LayoutInflater.from(context).inflate(layoutRes, this)

        closeIcon = inflateView.close.apply {
            setOnClickListener { closeAction?.invoke() }
        }
        textView = inflateView.text

        setOnClickListener { }

        isClickable = true

        background = context.getDrawable(R.drawable.chip_background_new)

        val dp8 = dp(8, context)
        setPadding(dp8, dp8, dp8, dp8)
    }

    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }

    fun setOnCloseIconClickListener(action: () -> Unit) {
        closeAction = action
    }

    fun setText(charSequence: CharSequence) {
        textView.text = charSequence
    }

    fun hideCloseIcon() {
        closeIcon.setGone()
    }

    fun setNonSelectable() {
        isClickable = false
    }

    override fun isChecked(): Boolean = isChipChecked

    override fun toggle() {
        isChipChecked = !isChipChecked
        refreshDrawableState()
    }

    override fun setChecked(state: Boolean) {
        isChipChecked = state
        refreshDrawableState()
    }

    private val CheckedStateSet = intArrayOf(android.R.attr.state_checked)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CheckedStateSet)
        }
        return drawableState
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }
}*/
