package net.igorbrejc.timeslack

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.common.collect.ImmutableList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.IllegalStateException
import java.util.*
import kotlin.concurrent.schedule

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

        initializeTimer()
        fetchModel()
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

    private fun initializeTimer() {
        timer.schedule(timerUpdateIntervalInMs, timerUpdateIntervalInMs) {
            runOnUiThread { updateView() }
        }
    }

    private fun fetchModel() {
        plan = SlackerPlan(
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

        val startTime = clock.now()
        activitiesLog = SlackerActivitiesLog(startTime, emptyList())
        deadline = SlackerTime.of(11, 0)
    }

    private fun updateView() {
        if (plan == null)
            throw IllegalStateException("Plan is not available.")

        if (deadline == null)
            throw IllegalStateException("Deadline is not available.")

        if (activitiesLog == null)
            throw IllegalStateException("Log of activities is not available.")

        val currentTime = clock.now()
        val model = RunningPlanModel(
            plan!!, deadline!!, activitiesLog!!, currentTime)

        val currentActivity = model.currentActivity()
        textCurrentActivity.text = currentActivity.activityName
        textCurrentActivityRemaining.text =
            model.currentActivityRemainingDuration().toString()
        textCurrentActivityFinishTime.text =
            model.currentActivityFinishTime().toString()

        val nextActivity = model.nextActivity()
        textNextActivity.text = when (nextActivity) {
            null -> ""
            else -> nextActivity.activityName
        }

        textSlackRemaining.text = model.slackDuration().toString()
        textPlanFinishTime.text = model.planFinishTime().toString()
    }

    private val clock: Clock = TimeMachine(20.0)
    private val timer: Timer = Timer()
    private val timerUpdateIntervalInMs = 3000L

    private var plan: SlackerPlan? = null
    private var activitiesLog: SlackerActivitiesLog? = null
    private var deadline: SlackerTime? = null
}
