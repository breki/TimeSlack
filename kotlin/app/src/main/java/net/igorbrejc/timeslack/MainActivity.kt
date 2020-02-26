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

        buttonForward.setOnClickListener {
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

    private fun fetchModel(): RunningFlowModel {
        val flow = Flow(
            ImmutableList.of(
                FlowActivity("prepare things", SlackerDuration(10)),
                FlowActivity("drive", SlackerDuration(15)),
                FlowActivity("prepare for hiking", SlackerDuration(5)),
                FlowActivity("hiking", SlackerDuration(60)),
                FlowActivity("unpack", SlackerDuration(5)),
                FlowActivity("drive", SlackerDuration(15)),
                FlowActivity("wash & dress", SlackerDuration(15))
            )
        )

        val startTime = clock.now()
        val activitiesLog = ActivitiesLog(startTime, emptyList())
        val deadline = SlackerTime.of(11, 0)
        return RunningFlowModel(flow, deadline, activitiesLog, startTime)
    }

    private fun processMessage(
        model: RunningFlowModel,
        message: UIMessage
    ): RunningFlowModel {
        val updatedModel =
            when (message) {
                is TimerUpdated -> {
                    model.withCurrentTime(message.currentTime)
                }
                is NextActivityButtonClicked ->
                    when (model.flowStatus()) {
                        is FlowRunningWithMoreActivities ->
                            model.finishCurrentActivity(message.currentTime)
                        is FlowRunningLastActivity ->
                            model.finishCurrentActivity(message.currentTime)
                        is FlowFinished -> {
                            // todo: do nothing for now, but in the future,
                            // we should return to the initial app activity
                            // (the flow selection)
                            model
                        }
                    }
            }

        lastModel = updatedModel
        updateView(updatedModel)
        return updatedModel
    }

    private fun updateView(model: RunningFlowModel) {
        when (val flowStatus = model.flowStatus()) {
            is FlowRunningWithMoreActivities -> {
                val currentActivity = flowStatus.currentActivity
                updateCurrentActivityView(model, currentActivity)

                setForwardButtonContent(
                    R.string.buttonForwardNextActivity,
                    R.drawable.ic_check_black_24dp)

                labelNextActivity.visibility = View.VISIBLE
                textNextActivity.visibility = View.VISIBLE
                val nextActivity = flowStatus.nextActivity
                textNextActivity.text = nextActivity.activityName
            }

            is FlowRunningLastActivity -> {
                val currentActivity = flowStatus.currentActivity
                updateCurrentActivityView(model, currentActivity)

                setForwardButtonContent(
                    R.string.buttonForwardFinishFlow,
                    R.drawable.ic_playlist_add_check_black_24dp)

                labelNextActivity.visibility = View.INVISIBLE
                textNextActivity.visibility = View.INVISIBLE
            }

            is FlowFinished -> {
                setForwardButtonContent(
                    R.string.buttonForwardClose,
                    R.drawable.ic_playlist_add_check_black_24dp)
            }
        }

        textSlackRemaining.text = model.slackDuration().toString()
        textFlowFinishTime.text = model.flowFinishTime().toString()
    }

    private fun updateCurrentActivityView(
        model: RunningFlowModel,
        currentActivity: FlowActivity
    ) {
        textCurrentActivity.text = currentActivity.activityName
        textCurrentActivityRemaining.text =
            model.currentActivityRemainingDuration().toString()
        textCurrentActivityFinishTime.text =
            model.currentActivityFinishTime().toString()
    }

    private fun setForwardButtonContent(
        textResourceId: Int,
        imageResourceId: Int
    ) {
        buttonForward.setText(textResourceId)
        buttonForward.setCompoundDrawablesWithIntrinsicBounds(
            imageResourceId, 0, 0, 0)
    }

    private val clock: Clock = TimeMachine(20.0)
    private val timer: Timer = Timer()
    private val timerUpdateIntervalInMs = 3000L

    private var lastModel: RunningFlowModel? = null
}
