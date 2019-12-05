package com.o1.timemanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    lateinit var sp: SharedPreferences
    lateinit var navigationView: NavigationView
    lateinit var user: JsonObject
    lateinit var api: Api
    var isLogin = false
    var username: String? = null
    var userAcnt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        sp = getSharedPreferences("info", Context.MODE_PRIVATE)
        isLogin = sp.getBoolean("isLogin", false)
        username = sp.getString("username", "Time Power")
        userAcnt = sp.getString("userAcnt", "Time Power")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
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

        val headerView = navigationView.getHeaderView(0)
        val avatar: ImageView = headerView.findViewById(R.id.avatar)
        avatar.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        val iconMenu: ImageView = findViewById(R.id.icon_menu)
        iconMenu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://121.36.56.36:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(Api::class.java)

        if (isLogin) {
            val textUsername: TextView = headerView.findViewById(R.id.username)
            textUsername.text = username

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
