package id.mjs.etalaseapp.ui.termcondition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.mjs.etalaseapp.R
import kotlinx.android.synthetic.main.activity_term_condition.*

class TermConditionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_term_condition)

        btn_back_term_condition.setOnClickListener {
            finish()
        }
    }
}