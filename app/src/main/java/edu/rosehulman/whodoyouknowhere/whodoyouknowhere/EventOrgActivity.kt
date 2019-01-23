package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.activity_event_org.*
import kotlinx.android.synthetic.main.add_event_dialog.view.*
import kotlinx.android.synthetic.main.app_bar_event_org.*

class EventOrgActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var events: ArrayList<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_org)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    fun buildEventCard() {

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.event_org, menu)
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

    private fun showAddDialog() {
        val builder = AlertDialog.Builder(this)
        //Configure the builder: title, icon, message/custom view OR list.  Buttons (pos, neg, neutral)
        builder.setTitle(getString(R.string.add_dialog_title))
        val view = LayoutInflater.from(this).inflate(R.layout.add_event_dialog, null, false)
        builder.setView(view)

        val toggleButton = view.findViewById<ToggleButton>(R.id.age_restriction_toggle)
        toggleButton?.setOnCheckedChangeListener { _, isChecked ->
            val msg = "Toggle Button is " + if (isChecked) "ON" else "OFF"

        }


        builder.setPositiveButton(android.R.string.ok) { _, _ ->

            val eventName = view.add_name_edit_text
            val eventDate = view.add_date_edit_text
            val eventLocation = view.add_location_edit_text
            val isAgeRestriction = view.age_restriction_toggle.isChecked
            val eventType = "Party"
           // val entryFee = view.add_entry_fee_edit_text.text.to(CurrencyAmount)
           // buildEvent(Event())

        }
        builder.setNegativeButton(android.R.string.cancel, null) // :)



        builder.create().show()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
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
}
