package com.ayu.archetecture.todoapp.tasks.ui

import android.app.Activity.RESULT_FIRST_USER
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ayu.archetecture.todoapp.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.tool_bar))

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        // Passing each menu ID as a set of Ids because each  menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_task_list,
            R.id.nav_task_statistics
        ), drawerLayout)

        //val navController = findNavController(R.id.nav_host_fragment) // 布局中将 FragmentContainerView 替换为 fragment 即可
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        // actionBar使用导航
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        // 侧边栏菜单导航到不同界面, navigation_graph中id与菜单项id相同来绑定
        navView.setupWithNavController(navController)

        // toolbar菜单与drawerLayout建立打开关闭联系，优先使用 onSupportNavigateUp 的导航控制
        /*var toolBar = findViewById<Toolbar>(R.id.tool_bar)
        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open, R.string.close )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()*/
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handles the Up button by delegating its behavior to the given NavController.
        // actionBar菜单项按钮点击事件委托navController处理
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}

const val ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1
