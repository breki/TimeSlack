package net.igorbrejc.timeslack

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.common.collect.ImmutableList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar
//                .make(view, "Still more slack time...", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        buttonNextActivity.setOnClickListener {
            val currentTime = clock.now()
            processMessage(
                lastModel!!,
                NextActivityButtonClicked(currentTime))
        }

        initializeTimer()
        lastModel = fetchModel()
        updateView(lastModel!!)
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
            val currentTime = clock.now()
            runOnUiThread { processMessage(
                lastModel!!,
                TimerUpdated(currentTime))
            }
        }
    }

    private fun fetchModel(): RunningPlanModel {
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

        val startTime = clock.now()
        val activitiesLog = SlackerActivitiesLog(startTime, emptyList())
        val deadline = SlackerTime.of(11, 0)
        return RunningPlanModel(plan, deadline, activitiesLog, startTime)
    }

    private fun processMessage(
        model: RunningPlanModel,
        message: SlackerMessage
    ): RunningPlanModel {
        val updatedModel =
            when (message) {
                is TimerUpdated -> {
                    model.withCurrentTime(message.currentTime)
                }
                is NextActivityButtonClicked ->
                    model.finishCurrentActivity(message.currentTime)
            }

        lastModel = updatedModel
        updateView(updatedModel)
        return updatedModel
    }

    private fun updateView(model: RunningPlanModel) {
        when (val planStatus = model.planStatus()) {
            is PlanRunningWithMoreActivities -> {
                val currentActivity = planStatus.currentActivity
                updateCurrentActivityView(model, currentActivity)

                setNextActivityButtonImage(R.drawable.ic_check_black_24dp)

                labelNextActivity.visibility = View.VISIBLE
                textNextActivity.visibility = View.VISIBLE
                val nextActivity = planStatus.nextActivity
                textNextActivity.text = nextActivity.activityName
            }

            is PlanRunningLastActivity -> {
                val currentActivity = planStatus.currentActivity
                updateCurrentActivityView(model, currentActivity)

                setNextActivityButtonImage(
                    R.drawable.ic_playlist_add_check_black_24dp)

                labelNextActivity.visibility = View.INVISIBLE
                textNextActivity.visibility = View.INVISIBLE
            }
        }

        textSlackRemaining.text = model.slackDuration().toString()
        textPlanFinishTime.text = model.planFinishTime().toString()
    }

    private fun updateCurrentActivityView(
        model: RunningPlanModel,
        currentActivity: SlackerActivity
    ) {
        textCurrentActivity.text = currentActivity.activityName
        textCurrentActivityRemaining.text =
            model.currentActivityRemainingDuration().toString()
        textCurrentActivityFinishTime.text =
            model.currentActivityFinishTime().toString()
    }

    private fun setNextActivityButtonImage(imageResourceId: Int) {
        buttonNextActivity.setImageResource(imageResourceId)
    }

    private val clock: Clock = TimeMachine(20.0)
    private val timer: Timer = Timer()
    private val timerUpdateIntervalInMs = 3000L

    private var lastModel: RunningPlanModel? = null
}
