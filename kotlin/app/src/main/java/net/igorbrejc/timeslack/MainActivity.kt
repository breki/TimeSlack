package net.igorbrejc.timeslack

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.common.collect.ImmutableList

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val plan = SlackerPlan(
            ImmutableList.of(
                SlackerActivity("prepare things", 10),
                SlackerActivity("drive", 15),
                SlackerActivity("prepare for hiking", 5),
                SlackerActivity("hiking", 60),
                SlackerActivity("unpack", 5),
                SlackerActivity("drive", 15),
                SlackerActivity("wash & dress", 15)
            )
        )

        val deadline = SlackerDeadline (SlackerTime.of(11, 0))

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar
                .make(view, "Still more slack time...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val currentActivityIndex = 0
        val currentActivity = plan.activities[currentActivityIndex]
        textCurrentActivity.text = currentActivity.activityName
        textCurrentActivityRemaining.text = "12"

        val nextActivityIndex = currentActivityIndex + 1
        val nextActivity = plan.activities[nextActivityIndex]
        textNextActivity.text = nextActivity.activityName
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
