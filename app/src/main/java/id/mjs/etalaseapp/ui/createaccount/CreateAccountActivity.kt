package id.mjs.etalaseapp.ui.createaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import id.mjs.etalaseapp.R
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        et_password_confirmation_register.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (et_password_register.text.toString() == et_password_confirmation_register.text.toString()){
                    text_confirmation.text = "Kata Sandi Sesuai"
                }
                else{
                    text_confirmation.text = "Kata Sandi Tidak Sesuai"
                }
            }

        })

        btn_to_login.setOnClickListener {
            finish()
        }

    }
}