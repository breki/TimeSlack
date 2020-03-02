package net.igorbrejc.timeslack

class RunningFlowModelBuilder {
    fun givenAFlow(): RunningFlowModelBuilder {
        flow = FlowBuilder().withSampleActivities().build()
        return this
    }

    fun andCurrentStep(stepIndex: Int, activityStartTime: SlackerTime):
            RunningFlowModelBuilder {

        var buildingLog = flow!!.startFlow(activityStartTime)
        for (x in 1..stepIndex) {
            buildingLog = buildingLog.finishStep(activityStartTime)
        }
        log = buildingLog

        activitiesLog = ActivitiesLog(
            flow!!.activities.count(),
            (0..stepIndex).map { activityStartTime })
        return this
    }

    fun andCurrentTimeOf(time: SlackerTime): RunningFlowModelBuilder {
        currentTime = time
        return this
    }

    fun build(): RunningFlowModel {
        return RunningFlowModel(
            flow!!, deadline!!, log!!, activitiesLog!!, currentTime!!)
    }

    fun andDeadlineOf(deadline: SlackerTime): RunningFlowModelBuilder {
        this.deadline = deadline
        return this
    }

    private var flow: Flow? = null
    private var deadline: SlackerTime? = null
    private var log: FlowLog? = null
    private var activitiesLog: ActivitiesLog? = null
    private var currentTime: SlackerTime? = null
}
