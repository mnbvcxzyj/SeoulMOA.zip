import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mobile.seoulmoa_zip.AboutActivity
import com.mobile.seoulmoa_zip.LikeActivity
import com.mobile.seoulmoa_zip.MainActivity

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

        // 서비스명 누르면 메인 액티비티로 이동하기
        val logoView: View? = toolbar.findViewById(R.id.logo)
        logoView?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_like -> {
                val intent = Intent(this, LikeActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_went -> {
                val intent = Intent(this, VisitedActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
