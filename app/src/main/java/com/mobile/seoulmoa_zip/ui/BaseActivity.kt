import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mobile.seoulmoa_zip.AboutActivity
import com.mobile.seoulmoa_zip.LikeActivity

import com.mobile.seoulmoa_zip.R
import com.mobile.seoulmoa_zip.VisitedActivity

open class BaseActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_like -> {
                val intent = Intent(this, LikeActivity::class.java)  // LikeActivity는 예시입니다. 실제 클래스명을 사용하세요.
                startActivity(intent)
                true
            }
            R.id.menu_went -> {
                val intent = Intent(this, VisitedActivity::class.java)  // WentActivity는 예시입니다. 실제 클래스명을 사용하세요.
                startActivity(intent)
                true
            }
            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java)  // AboutActivity는 예시입니다. 실제 클래스명을 사용하세요.
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
