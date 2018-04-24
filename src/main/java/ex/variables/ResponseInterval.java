/*
 * Copyright (c) 2018. Manuel D. Rossetti, manuelrossetti@gmail.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ex.variables;

import java.util.HashMap;
import java.util.Map;
import jsl.modeling.EventActionIfc;
import jsl.modeling.JSLEvent;
import jsl.modeling.ModelElement;
import jsl.modeling.SchedulingElement;
import jsl.modeling.elements.variable.Counter;
import jsl.observers.ModelElementObserver;
import jsl.utilities.statistic.WeightedStatisticIfc;
import jsl.modeling.elements.variable.ResponseVariable;

/**
 * This class represents an interval of time over which statistical collection
 * should be performed. An interval is specified by providing an interval start
 * time and a duration. The duration must be finite and greater than zero.
 *
 * Simulation responses in the form of instances of ResponseVariable,
 * TimeWeighted, and Counter should be added to the interval for observation.
 * New responses are created and associated with each of the supplied responses.
 * The new responses collect observations associated with the supplied responses
 * only during the specified interval.
 *
 * @author rossetti
 */
public class ResponseInterval extends SchedulingElement {

    /**
     * Need to ensure that start event happens after schedule start
     */
    public final int START_EVENT_PRIORITY = 15;

    /**
     * Need to ensure that end event happens before schedule end
     */
    public final int END_EVENT_PRIORITY = 5;

    /**
     * The action that represents the start of the interval
     */
    private EventActionIfc myStartAction;

    /**
     * The action that represents the end of the interval
     */
    private EventActionIfc myEndAction;

    /**
     * The event that represents the start of the interval
     */
    private JSLEvent myStartEvent;

    /**
     * The event that represents the end of the interval
     *
     */
    private JSLEvent myEndEvent;

    /**
     * A map of responses and the data associated with the interval
     */
    private Map<ResponseVariable, IntervalData> myResponses;

    /**
     * A map of counters and the data associated with the interval
     */
    private Map<Counter, IntervalData> myCounters;

    /**
     * An observer to handle the removal of the interval response if the
     * underlying response is removed from the model
     */
    private ModelElementObserver myObserver;

    /**
     * Intervals may be repeated. The represents the time that the interval last
     * started in time;
     *
     */
    private double myTimeLastStarted;

    /**
     * Intervals may be repeated. The represents the time that the interval last
     * started in time;
     *
     */
    private double myTimeLastEnded;

    /**
     * Indicates if the interval has been scheduled
     */
    private boolean myScheduledFlag;

    /**
     * The duration of the interval
     */
    private double myDuration;

    /**
     * The time that the interval should start
     */
    protected double myStartTime;

    protected ResponseSchedule myResponseSchedule;

    /**
     * Creates an interval response
     *
     * @param parent the parent model element
     * @param duration must be finite and strictly positive
     * @param label the label used to denote the interval, must not be null
     */
    public ResponseInterval(ModelElement parent, double duration, String label) {
        super(parent, label);
        if (label == null) {
            throw new IllegalArgumentException("The label must not be null.");
        }
        setStringLabel(label);
        setDuration(duration);
        myResponses = new HashMap<>();
        myObserver = new ResponseObserver();
        myCounters = new HashMap<>();
        myStartAction = new StartIntervalAction();
        myEndAction = new EndIntervalAction();
        myTimeLastStarted = 0.0;
        myTimeLastEnded = 0.0;
        myScheduledFlag = false;
        myStartTime = Double.NEGATIVE_INFINITY;
    }

    /**
     * Specifies when the interval is to start. If negative, then the interval
     * will not be started
     *
     * @param startTime must not be infinite
     */
    public final void setStartTime(double startTime) {
        if (startTime == Double.POSITIVE_INFINITY) {
            throw new IllegalArgumentException("The start time cannot be infinity");
        }
        myStartTime = startTime;
    }

    /**
     *
     * @return the time to start the schedule
     */
    public final double getStartTime() {
        return myStartTime;
    }

    /**
     *
     * @param schedule the response schedule that the interval is on
     */
    protected final void setResponseSchedule(ResponseSchedule schedule) {
        myResponseSchedule = schedule;
    }

    /**
     *
     * @param duration must be finite and positive
     */
    protected final void setDuration(double duration) {
        if (duration == Double.POSITIVE_INFINITY) {
            throw new IllegalArgumentException("The duration must be finite.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("The durationt must be > 0.");
        }
        myDuration = duration;
    }

    /**
     * Adds a ResponseVariable to the interval for data collection over the
     * interval
     *
     * @param theResponse the response to collect interval statistics on
     * @return a ResponseVariable for the interval
     */
    public ResponseVariable addResponseToInterval(ResponseVariable theResponse) {
        if (theResponse == null) {
            throw new IllegalArgumentException("The supplied response was null.");
        }
        if (myResponses.containsKey(theResponse)) {
            throw new IllegalArgumentException("The supplied response was already added.");
        }
        ResponseVariable rv = new ResponseVariable(this,
                theResponse.getName() + ":" + getStringLabel());
        IntervalData data = new IntervalData();
        data.myResponse = rv;
        myResponses.put(theResponse, data);
        theResponse.addObserver(myObserver);
        return rv;
    }

    /**
     * Adds a Counter to the interval for data collection over the interval
     *
     * @param theCounter the counter to collect interval statistics on
     * @return a ResponseVariable for the interval
     */
    public ResponseVariable addCounterToInterval(Counter theCounter) {
        if (theCounter == null) {
            throw new IllegalArgumentException("The supplied counter was null.");
        }
        if (myCounters.containsKey(theCounter)) {
            throw new IllegalArgumentException("The supplied counter was already added.");
        }
        ResponseVariable rv = new ResponseVariable(this,
                theCounter.getName() + ":" + getStringLabel());
        IntervalData data = new IntervalData();
        data.myResponse = rv;
        myCounters.put(theCounter, data);
        theCounter.addObserver(myObserver);
        return rv;
    }

    /**
     *
     * @return true if the interval has been scheduled
     */
    public final boolean isScheduled() {
        return myScheduledFlag;
    }

    /**
     * When the interval was last started
     *
     * @return When the interval was last started
     */
    public final double getTimeLastStarted() {
        return myTimeLastStarted;
    }

    /**
     * When the interval was last ended
     *
     * @return When the interval was last ended
     */
    public final double getTimeLastEnded() {
        return myTimeLastEnded;
    }

    /**
     * The duration (length) of the interval
     *
     * @return The duration (length) of the interval
     */
    public final double getDuration() {
        return myDuration;
    }

    @Override
    protected void initialize() {
//        System.out.println("In ResponseInterval: initialize()");
//        System.out.println("getStartTime() = " + getStartTime());
        super.initialize();
        if (getStartTime() >= 0.0) {
            scheduleInterval(getStartTime());
        }
    }

    @Override
    protected void afterReplication() {
//        System.out.println("In ResponseInterval: afterReplication()");
        super.afterReplication();
        myTimeLastStarted = 0.0;
        myTimeLastEnded = 0.0;
        cancelInterval();
    }

    /**
     * Schedules the interval to occur at current time + start time
     *
     * @param startTime the time to start the interval
     */
    protected final void scheduleInterval(double startTime) {
//        System.out.println("In ResponseInterval: scheduleInterval()");
//        System.out.println(" > Interval: " + getStringLabel());
//        System.out.println(" > scheduling interval to start at " + (getTime() + startTime));
        if (isScheduled()) {
            throw new IllegalStateException("Attempted to schedule an already scheduled interval");
        }
        myScheduledFlag = true;
        myStartEvent = scheduleEvent(myStartAction, startTime, START_EVENT_PRIORITY);
    }

    /**
     * Cancels the scheduling of the interval. Any statistical collection will
     * not occur.
     */
    public void cancelInterval() {
        myScheduledFlag = false;
        if (myStartEvent != null) {
            myStartEvent.setCanceledFlag(true);
        }
        if (myEndEvent != null) {
            myEndEvent.setCanceledFlag(true);
        }
        myStartEvent = null;
        myEndEvent = null;
    }

    @Override
    public String asString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Interval: ");
        sb.append(getStringLabel());
        sb.append(System.lineSeparator());
        sb.append("Start time: ");
        sb.append(getStartTime());
        sb.append(System.lineSeparator());
        sb.append("Time last started: ");
        sb.append(getTimeLastStarted());
        sb.append(System.lineSeparator());
        sb.append("Duration: ");
        sb.append(getDuration());
        sb.append(System.lineSeparator());
        sb.append("Time last ended: ");
        sb.append(getTimeLastEnded());
        sb.append(System.lineSeparator());
        sb.append("Is Scheduled: ");
        sb.append(isScheduled());
        sb.append(System.lineSeparator());
        sb.append("#Responses: ");
        sb.append(myResponses.size());
        sb.append(System.lineSeparator());
        sb.append("#Counters: ");
        sb.append(myCounters.size());
//        sb.append(System.lineSeparator());

        return sb.toString();
    }

    /**
     * Represents data collected at the start of an interval for use at the end
     * of the interval
     */
    class IntervalData {

        ResponseVariable myResponse;
        double mySumAtStart = 0.0;
        double mySumOfWeightsAtStart = 0.0;
        double myTotalAtStart = 0.0;
    }

    private class StartIntervalAction implements EventActionIfc {

        @Override
        public void action(JSLEvent evt) {
//            System.out.println("In StartIntervalAction: action()");
//            System.out.println("Interval:" + getStringLabel());
//            System.out.println(getTime() + " > capturing response data at start of interval");
            for (Map.Entry<ResponseVariable, IntervalData> entry : myResponses.entrySet()) {
                myTimeLastStarted = getTime();
                ResponseVariable key = entry.getKey();
                IntervalData data = entry.getValue();
                WeightedStatisticIfc w = key.getWithinReplicationStatistic();
                data.mySumAtStart = w.getWeightedSum();
                data.mySumOfWeightsAtStart = w.getSumOfWeights();
            }

            for (Map.Entry<Counter, IntervalData> entry : myCounters.entrySet()) {
                Counter key = entry.getKey();
                IntervalData data = entry.getValue();
                data.myTotalAtStart = key.getValue();
            }
//            System.out.println(getTime() + " > scheduling interval to end at " + (getTime() + getDuration()));
            myEndEvent = scheduleEvent(myEndAction, getDuration(), END_EVENT_PRIORITY);
        }

    }

    private class EndIntervalAction implements EventActionIfc {

        @Override
        public void action(JSLEvent evt) {
//            System.out.println("In EndIntervalAction: action()");
//            System.out.println("Interval:" + getStringLabel());
//            System.out.println(getTime() + " > capturing response data at end of interval");
            for (Map.Entry<ResponseVariable, IntervalData> entry : myResponses.entrySet()) {
                myTimeLastEnded = getTime();
                ResponseVariable key = entry.getKey();
                IntervalData data = entry.getValue();
                WeightedStatisticIfc w = key.getWithinReplicationStatistic();
                double sum = w.getWeightedSum() - data.mySumAtStart;
                double denom = w.getSumOfWeights() - data.mySumOfWeightsAtStart;
                if (denom != 0.0) {
                    double avg = sum / denom;
                    data.myResponse.setValue(avg);
                }
            }

            for (Map.Entry<Counter, IntervalData> entry : myCounters.entrySet()) {
                Counter key = entry.getKey();
                IntervalData data = entry.getValue();
                double intervalCount = key.getValue() - data.myTotalAtStart;
                data.myResponse.setValue(intervalCount);
            }

            if (myResponseSchedule != null) {
                myResponseSchedule.responseIntervalEnded(ResponseInterval.this);
            }
        }

    }

    private class ResponseObserver extends ModelElementObserver {

        @Override
        protected void removedFromModel(ModelElement m, Object arg) {
            // m is the model element that is being monitored that is
            // being removed from the model.
            // first remove the monitored object from the maps
            // then remove the associated response from the model
            if (m instanceof Counter) {
                IntervalData data = myCounters.remove((Counter) m);
                data.myResponse.removeFromModel();
            } else if (m instanceof ResponseVariable) {
                IntervalData data = myResponses.remove((ResponseVariable) m);
                data.myResponse.removeFromModel();
            }
        }

    }
}
