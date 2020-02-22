package net.igorbrejc.timeslack

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.common.collect.ImmutableList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar
                .make(view, "Still more slack time...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        updateView()
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

    private fun updateView() {

        val plan = SlackerPlan(
            ImmutableList.of(
                SlackerActivity("prepare things", SlackerDuration(10)),
                SlackerActivity("drive", SlackerDuration(15)),
                SlackerActivity("prepare for hiking", SlackerDuration(5)),
                SlackerActivity("hiking", SlackerDuration(60)),
                SlackerActivity("unpack", SlackerDuration(5)),
                SlackerActivity("drive", SlackerDuration(15)),
                SlackerActivity("wash & dress", SlackerDuration(15))
            )
        )

        val activitiesLog = SlackerActivitiesLog(
            SlackerTime.of(9, 0),
            ImmutableList.of(SlackerTime.of(9, 15)))
        val deadline = SlackerTime.of(11, 0)
        val currentTime = SlackerTime.now()
        val model = RunningPlanModel(
            plan, deadline, activitiesLog, currentTime)

        val currentActivity = model.currentActivity()
        textCurrentActivity.text = currentActivity.activityName
        textCurrentActivityRemaining.text =
            model.currentActivityRemainingDuration().toString()

        val nextActivity = model.nextActivity()
        textNextActivity.text = when (nextActivity) {
            null -> ""
            else -> nextActivity.activityName
        }

        textSlackRemaining.text = model.slackDuration().toString()
        textPlanFinishTime.text = model.planFinishTime().toString()
    }
}
