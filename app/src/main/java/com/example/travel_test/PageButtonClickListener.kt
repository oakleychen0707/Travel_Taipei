import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import com.example.travel_test.*

class PageButtonClickListener(
    private val context: Context,
    private val supportFragmentManager: FragmentManager,
    private val currentActivity: Class<*>,
    private val lang: String // 新增 lang 參數
) {
    fun setupButtons(page1Button: ImageButton, page2Button: ImageButton, page3Button: ImageButton, page4Button: ImageButton) {
        page1Button.setOnClickListener {
            if (currentActivity != MainActivity::class.java) {
                Log.d("MainActivity", "page1 button clicked")
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }

        page2Button.setOnClickListener {
            if (currentActivity != SecondActivity::class.java) {
                Log.d("MainActivity", "page2 button clicked")
                val intent = Intent(context, SecondActivity::class.java).apply {
                    putExtra("lang", lang) // 使用傳遞過來的 lang 參數
                }
                context.startActivity(intent)
            }
        }

        page3Button.setOnClickListener {
            if (currentActivity != ThirdActivity::class.java) {
                Log.d("MainActivity", "page3 button clicked")
                val intent = Intent(context, ThirdActivity::class.java)
                context.startActivity(intent)
            }
        }

        page4Button.setOnClickListener {
            if (currentActivity != FourthActivity::class.java) {
                Log.d("MainActivity", "page4 button clicked")
                val intent = Intent(context, FourthActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}