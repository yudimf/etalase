package id.mjs.etalaseapp.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import id.mjs.etalaseapp.R
import id.mjs.etalaseapp.ui.changepassword.ChangePasswordActivity
import id.mjs.etalaseapp.ui.login.LoginActivity
import id.mjs.etalaseapp.ui.main.MainActivity
import id.mjs.etalaseapp.ui.myprofile.MyProfileActivity
import id.mjs.etalaseapp.ui.termcondition.TermConditionActivity
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    lateinit var sharedPreferences : SharedPreferences
    private lateinit var jwt : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = context?.getSharedPreferences("UserPref", Context.MODE_PRIVATE)!!
        jwt = sharedPreferences.getString("token", "").toString()

        btn_my_profile.setOnClickListener {
            if (jwt.isEmpty()){
//                showAlertLogin()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(context, MyProfileActivity::class.java)
                startActivity(intent)
            }
        }
        btn_change_password.setOnClickListener {
            if (jwt.isEmpty()){
//                showAlertLogin()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(context, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
        }
        btn_term_and_condition.setOnClickListener{
            val intent = Intent(context, TermConditionActivity::class.java)
            startActivity(intent)
        }

        if (jwt.isEmpty()){
            btn_logout.visibility = View.GONE
        }
        btn_logout.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setMessage("Log out ?")
            alertDialogBuilder.setPositiveButton("Oke") { _, _ ->
                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                activity?.finish()
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
//                    Toast.makeText(this@DownloadActivity,"No Clicked",Toast.LENGTH_SHORT).show()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun showAlertLogin(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Login untuk melanjutkan ?")
        alertDialogBuilder.setPositiveButton("Oke") { _, _ ->
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
//                    Toast.makeText(this@DownloadActivity,"No Clicked",Toast.LENGTH_SHORT).show()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}