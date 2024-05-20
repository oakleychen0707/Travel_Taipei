import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import com.example.travel_test.*

class PageButtonClickListener(
    private val context: Context,
    private val currentActivity: Class<*>,
    private val lang: String // 新增 lang 參數
) {
    fun setupButtons(vararg buttons: ImageButton) {
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val targetActivity = when (index) {
                    0 -> MainActivity::class.java
                    1 -> SecondActivity::class.java
                    2 -> ThirdActivity::class.java
                    3 -> FourthActivity::class.java
                    else -> return@setOnClickListener
                }

                if (currentActivity != targetActivity) {
                    Log.d("MainActivity", "page${index + 1} button clicked")
                    val intent = Intent(context, targetActivity).apply {
                        putExtra("lang", lang) // 使用傳遞過來的 lang 參數
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}