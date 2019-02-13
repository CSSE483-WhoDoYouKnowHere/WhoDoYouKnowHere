package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.mindorks.placeholderview.SwipeDecor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_user_dialog.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener, EventOrgFragment.OnEventOrgFragmentSelectedListener {


    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var viewManager: LinearLayoutManager
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private var uid: String? = null
    private var backButtonCount = 0
    private val ageMinimum = 18
    private val userRef = FirebaseFirestore
        .getInstance()
        .collection(Constants.USERS_COLLECTION)

    private val margin = 160 //160
    private val animationDuration = 300
    private var isToUndo = false

    init {

        uid = user!!.uid
        userRef.whereEqualTo("id", uid)
            .limit(1).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    Log.d(Constants.TAG, "User exists with UID: $uid")
                    val isEmpty = task.result!!.isEmpty
                } else {
                    Log.d(Constants.TAG, "User does not exist. UID: $uid \nProceeding to add User object to Firebase")
                    addUser()
                }
            })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        bottom_nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        val completeListener = OnCompleteListener<AuthResult> {

            @NonNull
            fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful()) {
                    var isNew = task.result?.additionalUserInfo?.isNewUser
                    Log.d("MyTAG", "onComplete: $isNew")
                }
            }
        }

        val s = FirebaseAuth.getInstance().getAccessToken(true)




        setupSwipeView()
        initializeListeners()

        val extras = intent.extras
        if (extras != null) {
            uid = extras.getString(Constants.UID)
        }

        val bottomMargin = Utils.dpToPx(margin)
        val windowSize = Utils.getDisplaySize(windowManager)
        swipeView!!.builder
            .setDisplayViewCount(3)
            .setIsUndoEnabled(true)
            .setSwipeVerticalThreshold(Utils.dpToPx(50))
            .setSwipeHorizontalThreshold(Utils.dpToPx(50))
            .setHeightSwipeDistFactor(10f)
            .setWidthSwipeDistFactor(5f)
            .setSwipeDecor(
                SwipeDecor()
                    .setViewWidth(windowSize.x)
                    .setViewHeight(windowSize.y - bottomMargin)
                    .setViewGravity(Gravity.TOP)
                    .setPaddingTop(20)
                    .setSwipeAnimTime(animationDuration)
                    .setRelativeScale(0.01f)
                    .setSwipeInMsgLayoutId(R.layout.card_event_swipe_in)
                    .setSwipeOutMsgLayoutId(R.layout.card_event_swipe_out)
            )


        val cardViewHolderSize = Point(windowSize.x, windowSize.y - bottomMargin)

        for (event in Utils.getSampleEvents()) {
            swipeView!!.addView(EventCard(this, event, cardViewHolderSize))
        }

        rejectBtn.setOnClickListener({ swipeView!!.doSwipe(false) })

        acceptBtn.setOnClickListener({ swipeView!!.doSwipe(true) })

        undoBtn.setOnClickListener({ swipeView!!.undoLastSwipe() })

        swipeView!!.addItemRemoveListener {
            if (isToUndo) {
                isToUndo = false
                swipeView!!.undoLastSwipe()
            }
        }
//        viewManager = LinearLayoutManager(this)
//        eventAdapter = EventAdapter(this, viewManager)
//
//        recyclerView = findViewById<RecyclerView>(R.id.event_recycler_view).apply {
//
//            setHasFixedSize(true)
//
//            layoutManager = viewManager
//
//            adapter = eventAdapter
//
//        }
//
//        val callback = SimpleItemTouchHelperCallback(eventAdapter)
//        mItemTouchHelper = ItemTouchHelper(callback)
//        mItemTouchHelper.attachToRecyclerView(recyclerView)

        fab.hide()
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )


        drawer_layout.addDrawerListener(
            object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {
                    // do nothing
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    // do nothing
                }

                override fun onDrawerOpened(drawerView: View) {
                    drawerView.sign_out_button.setOnClickListener {
                        Log.d(Constants.TAG, "Sign out button clicked")
                        auth.signOut()
                    }
                    drawerView.account_edit_button.setOnClickListener {
                        editProfileDialog()
                    }
                }

                override fun onDrawerClosed(drawerView: View) {
                    //do nothing
                }
            })
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    fun addUser() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.user_profile_dialog_title))
        val view = LayoutInflater.from(this).inflate(R.layout.add_user_dialog, null, false)
        builder.setView(view)

        val userId = uid.toString()
        val name = view.user_name_edit_text.text.toString()
        val sexSpinner = view.sex_drop_down

        val dateButton = view.select_date_button
        val age: Int = 9
        dateButton.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { picker, year, month, day ->
                picker
            }, now[Calendar.YEAR - ageMinimum], now[Calendar.MONTH], now[Calendar.DATE])
            datePicker.show()
        }


        var user = User(userId, name, age, "male", 0, "gsoignsigs", ArrayList())
        userRef.add(user)
        builder.create().show()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }


    private fun initializeListeners() {
        authListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            Log.d(Constants.TAG, "User: $user")
            if (user == null) {
                switchToLoginActivity()
            }
        }
    }

    private fun switchToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (backButtonCount >= 1) {
            finish()
        } else if (backButtonCount < 1) {
            Toast.makeText(this, getString(R.string.back_button_exit_toast), Toast.LENGTH_SHORT)
                .show()
            backButtonCount++
        } else if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)

        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bottom_nav_event_org -> {
                // startEventOrgActivity(uid!!)
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.main_fragment_layout, EventOrgFragment(), Constants.EVENT_ORG_FRAGMENT)
                ft.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.bottom_nav_messages -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_hosted_events -> {
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun startEventOrgActivity(uid: String) {
        val intent = Intent(this, EventOrgActivity(uid)::class.java)
        //intent.putExtra("something", true)
        startActivity(intent)
        finish()
    }

    private fun editProfileDialog() {

    }

    override fun onEventOrgFragmentSelected(event: Event) {
        val eventId = event.id
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(
            R.id.main_fragment_layout,
            AttendeeListFragment.newInstance(eventId),
            Constants.ATTENDEE_LIST_FRAGMENT
        )
        ft.addToBackStack(Constants.ATTENDEE_LIST_FRAGMENT)
        ft.commit()

    }

    fun getFab(): FloatingActionButton {
        return fab
    }

    private fun setupSwipeView() {
//        mSwipeView = findViewById(R.id.swipeView)
//
//        mSwipeView.getBuilder<SwipePlaceHolderView, SwipeViewBuilder<SwipePlaceHolderView>>()
//            .setDisplayViewCount(3)
//            .setSwipeDecor(
//                SwipeDecor()
//                    .setPaddingTop(20)
//                    .setRelativeScale(0.01f)
//                    .setSwipeInMsgLayoutId(R.layout.card_event_swipe_in)
//                    .setSwipeOutMsgLayoutId(R.layout.card_event_swipe_out)
//            )
//
//
//        for (i in 1..5) {
//            var event = Utils.getSampleEvents()
//            mSwipeView.addView(EventCard(this, event, mSwipeView))
//        }
//
//        findViewById<ImageButton>(R.id.rejectBtn).setOnClickListener { mSwipeView.doSwipe(false) }
//
//        findViewById<ImageButton>(R.id.acceptBtn).setOnClickListener { mSwipeView.doSwipe(true) }
    }
}
