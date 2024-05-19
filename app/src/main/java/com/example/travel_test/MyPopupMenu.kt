import android.content.Context
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu

class MyPopupMenu(context: Context, anchor: View) : PopupMenu(context, anchor) {

    init {
        try {
            val mFieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            mFieldPopup.isAccessible = true
            val mPopup = mFieldPopup.get(this)
            if (mPopup != null) {
                val setShowIcon = mPopup.javaClass.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                setShowIcon.invoke(mPopup, true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    constructor(context: Context, anchor: View, gravity: Int) : this(context, anchor)

    constructor(context: Context, anchor: View, gravity: Int, popupStyleAttr: Int, popupStyleRes: Int) : this(context, anchor)

    override fun show() {
        super.show()
    }
}
