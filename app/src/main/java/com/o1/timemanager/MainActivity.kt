package com.o1.timemanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sp = getSharedPreferences("info", Context.MODE_PRIVATE)
        val islogin = sp.getBoolean("isLogin", false)
        val uid = sp.getInt("uid", 0)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_information, R.id.nav_knapsack,
            R.id.nav_setting, R.id.nav_share, R.id.nav_send
        )
            .setDrawerLayout(drawer)
            .build()
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navigationView, navController)

        val avatar: ImageView = navigationView.getHeaderView(0).findViewById(R.id.avatar)
        avatar.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        val iconMenu: ImageView = findViewById(R.id.icon_menu)
        iconMenu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp())
    }
}
