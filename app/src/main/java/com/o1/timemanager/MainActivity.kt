package com.o1.timemanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import com.o1.timemanager.ui.backpack.BackpackFragment
import com.o1.timemanager.ui.history.HistoryFragment
import com.o1.timemanager.ui.home.HomeFragment
import com.o1.timemanager.ui.lottery.LotteryFragment
import com.o1.timemanager.ui.team.InTeamFragment
import com.o1.timemanager.ui.team.OutTeamFragment
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    lateinit var sp: SharedPreferences
    lateinit var navigationView: NavigationView
    lateinit var user: JsonObject
    lateinit var api: Api
    var isLogin = false
    var username: String = ""
    var userAcnt: String = ""
    lateinit var homeFragment: Fragment
    lateinit var backpackFragment: Fragment
    lateinit var lotteryFragment: Fragment
    lateinit var inTeamFragment: Fragment
    lateinit var outTeamFragment: Fragment
    lateinit var currentFragment: Fragment
    lateinit var historyFragment: Fragment
    var minutes: Int = 0
    var seconds: Int = 0
    lateinit var circle: Circle
    var teamUUID = ""
    lateinit var conn: Connection
    lateinit var channel: Channel
    var isCaptain = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getSharedPreferences("info", Context.MODE_PRIVATE)
        isLogin = sp.getBoolean("isLogin", false)
        username = sp.getString("username", "Time Power").toString()
        userAcnt = sp.getString("userAcnt", "Time Power").toString()

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
//        NavigationUI.setupWithNavController(navigationView, navController)
        homeFragment = HomeFragment()
        backpackFragment = BackpackFragment()
        lotteryFragment = LotteryFragment()
        inTeamFragment = InTeamFragment()
        outTeamFragment = OutTeamFragment()
        historyFragment = HistoryFragment()

        currentFragment = homeFragment

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    switchFragment(homeFragment).commit()
                }
                R.id.nav_information -> {
                    switchFragment(backpackFragment).commit()
                }
                R.id.nav_knapsack -> {
                    switchFragment(lotteryFragment).commit()
                }
                R.id.nav_team -> {
                    switchFragment(outTeamFragment).commit()
                }
                R.id.nav_history -> {
                    switchFragment(historyFragment).commit()
                }
                else -> {
                }
            }
            drawer.closeDrawer(navigationView)
            true
        }

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

    fun leaveTeam() {
        currentFragment = if (isCaptain) {
            homeFragment
        } else {
            outTeamFragment
        }
        supportFragmentManager.beginTransaction().remove(inTeamFragment).show(currentFragment).commit()
        teamUUID = ""
    }

    fun joinTeam(teamUUID: String, isCaptain: Boolean = false) {

        if (teamUUID.matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"))) {
            this.teamUUID = teamUUID
            this.isCaptain = isCaptain

            switchFragment(inTeamFragment).commit()
        }
        else {
            Toast.makeText(this, "${teamUUID}不是队伍编号", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        when {
//            currentFragment == inTeamFragment -> {
//                leaveTeam()
//            }
            currentFragment != homeFragment -> {
                switchFragment(homeFragment).commit()
            }
            else -> {
                super.onBackPressed()
            }
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

    fun switchFragment(fragment: Fragment): FragmentTransaction {
        val transaction = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            if (::currentFragment.isInitialized) {
                transaction.hide(currentFragment)
            }
            transaction.add(R.id.nav_host_fragment, fragment, fragment.javaClass.name)
        }
        else {
            transaction.hide(currentFragment).show(fragment)
        }
        currentFragment = fragment
        return transaction
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult.contents == null) {
            Toast.makeText(this, "扫码失败", Toast.LENGTH_SHORT).show()
        }
        else {
            joinTeam(intentResult.contents)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}
