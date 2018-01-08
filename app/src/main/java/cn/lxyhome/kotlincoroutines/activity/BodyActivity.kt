package cn.lxyhome.kotlincoroutines.activity

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import cn.lxyhome.kotlincoroutines.R
import cn.lxyhome.kotlincoroutines.fragment.BlankFragment
import kotlinx.android.synthetic.main.activity_body.*

class BodyActivity : AppCompatActivity(), BlankFragment.OnFragmentInteractionListener {


    private val bloanfragment: BlankFragment by lazy {
        creatFragment()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                HideBloanFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                HideBloanFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                ShowBloanFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun ShowBloanFragment() {
        val beginTransaction = fragmentManager.beginTransaction()
        if (fragmentManager.findFragmentByTag("bloanfragment") == null) {
            beginTransaction.add(R.id.parent_Fragment,bloanfragment, "bloanfragment")
        }
        beginTransaction.show(bloanfragment)
        beginTransaction.commit()
    }

    private fun HideBloanFragment() {
        val beginTransaction = fragmentManager.beginTransaction()
        if (fragmentManager.findFragmentByTag("bloanfragment") == null) {
            beginTransaction.add(R.id.parent_Fragment,bloanfragment, "bloanfragment")
        }

        beginTransaction.hide(bloanfragment)
        beginTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body)
        init()
        registerListener()

    }

    private fun init() {

    }

    private fun creatFragment(): BlankFragment = BlankFragment.newInstance()


    private fun registerListener() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
