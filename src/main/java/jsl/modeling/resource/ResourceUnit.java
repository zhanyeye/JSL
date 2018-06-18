/*
 * Copyright (c) 2018. Manuel D. Rossetti, rossetti@uark.edu
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
package jsl.modeling.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jsl.modeling.EventActionIfc;
import jsl.modeling.JSLEvent;
import jsl.modeling.ModelElement;
import jsl.modeling.SchedulingElement;
import jsl.modeling.State;
import jsl.modeling.elements.Schedule;
import jsl.modeling.elements.ScheduleChangeListenerIfc;
import jsl.modeling.elements.variable.Counter;
import jsl.modeling.elements.variable.ResponseVariable;
import jsl.modeling.queue.Queue;
import jsl.modeling.queue.Queue.Discipline;
import jsl.modeling.queue.QueueListenerIfc;
import jsl.modeling.queue.QueueResponse;
import jsl.modeling.resource.Request.PreemptionRule;
import jsl.utilities.GetValueIfc;
import jsl.utilities.random.RandomIfc;
import jsl.utilities.random.distributions.Constant;

/**
 * A ResourceUnit is a single unit of a resource. A resource is something that
 * is needed to allocate in the system. A resource unit can be idle (not allocated),
 * busy (allocated), inactive (not available for allocation), failed (not
 * available for
 * allocation due to failure. The resource is initialized to be idle.
 * Thus,
 * prior to and starting at time zero, the resource is idle.
 * <p>
 * A ResourceUnit might have failures. If the failures that it permits can be
 * delayed when they occur while in the busy state, then the resource failure
 * option should be true.
 *
 * @author rossetti
 */
public class ResourceUnit extends SchedulingElement implements SeizeableIfc {

    private final ResourceState myBusyState = new Busy();
    private final ResourceState myIdleState = new Idle();
    private final ResourceState myFailedState = new Failed();
    private final ResourceState myInactiveState = new Inactive();

    private double myStartTime;
    private final boolean myAutoStartFailuresFlag;
    private final Queue<Request> myRequestQ;
    private final boolean myFailureDelayOption;
    private final boolean myInactivePeriodDelayOption;
    private final Discipline myFailureQDiscipline;
    private final boolean myFailureQStatOption;
    private final EndRequestUsageAction myEndRequestUsageAction = new EndRequestUsageAction();
    private EndDownTimeAction myEndDownTimeAction;

    private Request myCurrentRequest;
    private JSLEvent myCurrentRequestEvent;
    private Request myPreemptedRequest;
    private ResourceState myCurrentState;
    private ResourceState myPrevState;
    private List<FailureProcess> myFailureProcesses;
    private Queue<FailureNotice> myFailureNoticeQ;
    private FailureNotice myCurrentFailureNotice;
    private InactivePeriodNotice myCurrentInactivePeriodNotice;
    private InactivePeriodNotice myPendingInactivePeriodNotice;
    private JSLEvent myCurrentDownTimeEvent;
    private final ResponseVariable myUtil;
    private int myNumSeizes;
    private ScheduleListener myScheduleListener;
    private final EndInactivePeriodAction myEndInactivePeriodAction = new EndInactivePeriodAction();
    private JSLEvent myCurrentInactivePeriodEvent;
    private final boolean myCollectStateStats;
    private ResponseVariable myFailedProp;
    private ResponseVariable myInactiveProp;
    private ResponseVariable myIdleProp;
    private final boolean myRequestStatOption;
    private final boolean myRequestQCancelStatOption;
    private ResponseVariable myReqTimeToCompletion;
    private Counter myNumRejected;
    private Counter myNumCanceled;
    private Counter myNumPreemptions;
    private Counter myTotalPreemptTime;
    private Counter myNumCompleted;
    private ResponseVariable myPreemptProb;
    private ResponseVariable myPreemptTime;

    /**
     * It is highly recommended that the Resource.Builder class be used to
     * construction ResourceUnits. This generic public method is available
     * primarily to simplify sub-classing
     *
     * @param parent the model element parent
     * @param name the name
     * @param autoStartFailuresFlag whether to start failures automatically
     * @param failureDelayOption whether failures can delay
     * @param failuresQDiscipline the discipline for failures
     * @param failureQStatOption whether failure enterWaitingState statistics
     * are
     * collected
     * @param requestQCancelStatOption whether request statistics are collected
     * when the request is canceled
     * @param inactivePeriodDelayOption whether inactive periods can delay
     * @param stateStatOption whether statistics are collected on resource
     * states
     * @param requestStatOption whether request statistics are collected
     * @param requestQDiscipline the enterWaitingState discipline for requests
     * @param requestQStatsOption whether enterWaitingState statistics are
     * collected
     */
    public ResourceUnit(ModelElement parent, String name,
            boolean autoStartFailuresFlag, boolean failureDelayOption,
            Discipline failuresQDiscipline, boolean failureQStatOption,
            boolean requestQCancelStatOption, boolean inactivePeriodDelayOption,
            boolean stateStatOption, boolean requestStatOption,
            Discipline requestQDiscipline, boolean requestQStatsOption) {
        super(parent, name);
        myAutoStartFailuresFlag = autoStartFailuresFlag;
        myFailureDelayOption = failureDelayOption;
        myFailureQDiscipline = failuresQDiscipline;
        myFailureQStatOption = failureQStatOption;
        myRequestQCancelStatOption = requestQCancelStatOption;
        myInactivePeriodDelayOption = inactivePeriodDelayOption;
        myCollectStateStats = stateStatOption;
        myRequestStatOption = requestStatOption;
        myRequestQ = new Queue<>(this, getName() + ":RequestQ",
                requestQDiscipline, requestQStatsOption);
        myUtil = new ResponseVariable(this, getName() + ":PTimeBusy");
        if (getCollectStateStatsOption()) {
            myFailedProp = new ResponseVariable(this, getName() + ":PTimeFailed");
            myInactiveProp = new ResponseVariable(this, getName() + ":PTimeInactive");
            myIdleProp = new ResponseVariable(this, getName() + ":PTimeIdle");
        }
        if (getRequestStatisticsOption()) {
            myReqTimeToCompletion = new ResponseVariable(this, getName() + ":ReqTimeToCompletion");
            myNumRejected = new Counter(this, getName() + ":NumRejected");
            myNumCanceled = new Counter(this, getName() + ":NumCanceled");
            myNumPreemptions = new Counter(this, getName() + ":NumPreemptions");
            myTotalPreemptTime = new Counter(this, getName() + ":TotalPreempTime");
            myNumCompleted = new Counter(this, getName() + ":NumCompleted");
            myPreemptProb = new ResponseVariable(this, getName() + ":ProbOfPreemption");
            myPreemptTime = new ResponseVariable(this, getName() + ":AvgPreemptTime");
        }
        myCurrentState = myIdleState;
        myPrevState = null;
    }

    public static class Builder {

        private final ModelElement parent;
        private String name = null;
        private boolean autoStartFailuresFlag = false;
        private boolean failureDelayOption = false;
        private boolean inactivePeriodDelayOption = false;
        private Discipline requestQDiscipline = Discipline.FIFO;
        private Discipline failuresQDiscipline = Discipline.FIFO;
        private boolean requestQStatsOption = false;
        private boolean failureQStatOption = false;
        private boolean stateStatOption = false;
        private boolean requestStatOption = false;
        private boolean requestQCancelStatOption = false;

        /**
         *
         * @param parent the parent model element
         */
        public Builder(ModelElement parent) {
            this.parent = parent;
        }

        /**
         *
         * @param name the name of the unit
         * @return the Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Turn on auto starting of failures. Default is off.
         *
         * @return the Builder
         */
        public Builder autoStartFailures() {
            autoStartFailuresFlag = true;
            return this;
        }

        /**
         * Allow the failures to be delayed. Default is not delayed
         *
         * @return the Builder
         */
        public Builder allowFailuresToDelay() {
            failureDelayOption = true;
            return this;
        }

        /**
         * Allow the inactive period events to delay. The default is not delayed
         *
         * @return the Builder
         */
        public Builder allowInactivePeriodsToDelay() {
            inactivePeriodDelayOption = true;
            return this;
        }

        /**
         * Turn on collection of statistics to include those requests that were
         * canceled. The default is not to include the canceled requests in the
         * request enterWaitingState statistics
         *
         * @return the Builder
         */
        public Builder collectCanceledRequestQStatistics() {
            requestQCancelStatOption = true;
            return this;
        }

        /**
         * Turn on collection of state statistics. The default is no state
         * statistics.
         *
         * @return the Builder
         */
        public Builder collectStateStatistics() {
            stateStatOption = true;
            return this;
        }

        /**
         * Turn on the collection of request enterWaitingState statistics. The
         * default
         * is no
         * collection.
         *
         * @return the Builder
         */
        public Builder collectRequestStatistics() {
            requestStatOption = true;
            return this;
        }

        /**
         * The default is FIFO
         *
         * @param discipline the enterWaitingState discipline
         * @return the Builder
         */
        public Builder requestQueueDiscipline(Discipline discipline) {
            requestQDiscipline = discipline;
            return this;
        }

        /**
         * The default is FIFO
         *
         * @param discipline the enterWaitingState discipline
         * @return the Builder
         */
        public Builder failureQueueDiscipline(Discipline discipline) {
            failuresQDiscipline = discipline;
            return this;
        }

        /**
         * Turn on collection of request enterWaitingState statistics. The
         * default is
         * no
         * collection.
         *
         * @return the Builder
         */
        public Builder collectRequestQStats() {
            requestQStatsOption = true;
            return this;
        }

        /**
         * Turn on collection of failure enterWaitingState statistics. The
         * default is
         * no
         * collection.
         *
         * @return the Builder
         */
        public Builder collectFailureQStats() {
            failureQStatOption = true;
            return this;
        }

        /**
         * Cause the ResourceUnit to be built
         *
         * @return the created ResourceUnit
         */
        public ResourceUnit build() {
            return new ResourceUnit(parent, name,
                    autoStartFailuresFlag, failureDelayOption,
                    failuresQDiscipline, failureQStatOption,
                    requestQCancelStatOption, inactivePeriodDelayOption,
                    stateStatOption, requestStatOption,
                    requestQDiscipline, requestQStatsOption);
        }

        /**
         * Builds a list with the specified number of resource timeUnits all
         * with the same specifications, named getName():Unit:&#35;
         *
         * @param numToBuild the number to build
         * @return the filled up list
         */
        public List<ResourceUnit> build(int numToBuild) {
            if (numToBuild <= 0) {
                throw new IllegalArgumentException("The number to build must be > 0");
            }
            List<ResourceUnit> list = new ArrayList<>();

            for (int i = 1; i <= numToBuild; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(name).append(":").append(i);
                ResourceUnit resourceUnit = new ResourceUnit(parent, sb.toString(),
                        autoStartFailuresFlag, failureDelayOption,
                        failuresQDiscipline, failureQStatOption,
                        requestQCancelStatOption, inactivePeriodDelayOption,
                        stateStatOption, requestStatOption,
                        requestQDiscipline, requestQStatsOption);
                list.add(resourceUnit);
            }
            return list;
        }
    }

    @Override
    protected void initialize() {
        // clear out the states
        myStartTime = 0.0;
        myNumSeizes = 0;
        myIdleState.initialize();
        myBusyState.initialize();
        myFailedState.initialize();
        myInactiveState.initialize();
        // there is no current request
        myCurrentRequest = null;
        myCurrentRequestEvent = null;
        myCurrentFailureNotice = null;
        myCurrentDownTimeEvent = null;
        myCurrentInactivePeriodNotice = null;
        myPendingInactivePeriodNotice = null;
        myCurrentInactivePeriodEvent = null;
        // there is no preempted request
        myPreemptedRequest = null;
        // there is no previous state
        myPrevState = null;
        // the current state is initialized to the idle state
        myCurrentState = myIdleState;
        // the state gets entered at the current time
        myCurrentState.enter(getTime());
        notifyUpdateObservers();
        // if failures start automatically, start them
        if (getAutoFailuresFlag() == true) {
            for (FailureProcess fe : myFailureProcesses) {
                fe.startFailureProcess();
            }
        }
    }

    @Override
    protected void warmUp() {
        super.warmUp();
        myStartTime = getTime();
        myCurrentState.exit(getTime());
        myBusyState.resetStateCollection();
        myIdleState.resetStateCollection();
        myFailedState.resetStateCollection();
        myInactiveState.resetStateCollection();
        myCurrentState.enter(getTime());
    }

    @Override
    protected void replicationEnded() {
        double t = getTotalTime();
        if (t > 0) {
            double util = getTotalTimeBusy() / t;
            myUtil.setValue(util);
            if (myCollectStateStats) {
                myFailedProp.setValue(getTotalTimeFailed() / t);
                myInactiveProp.setValue(getTotalTimeInactive() / t);
                myIdleProp.setValue(getTotalTimeIdle() / t);
            }
        }
    }

    /**
     * Creates and adds a TimeBasedFailure to the ResourceUnit. Will throw an
     * IllegalArgumentException if the failure delay option of the
     * FailureElement is inconsistent with that permitted by the unit
     *
     * @param repairTime the time to spend down
     * @param timeToFail the time between failures
     * @param delayOption true if the failures are allowed to be delayed
     * @return
     */
    public TimeBasedFailure addTimeBasedFailure(RandomIfc repairTime,
            RandomIfc timeToFail, boolean delayOption) {
        TimeBasedFailure tbf = new TimeBasedFailure(this, repairTime,
                timeToFail, delayOption);
        addFailureElement(tbf);
        return tbf;
    }

    /**
     * Will throw an IllegalArgumentException if the failure delay option of the
     * FailureElement is inconsistent with that permitted by the unit
     *
     * @param fe the element to add, must not be null, and must not have already been added
     */
    public void addFailureElement(FailureProcess fe) {
        if (fe == null){
            throw new IllegalArgumentException("The supplied FailureElement was null");
        }
        if (fe.getFailureDelayOption() != getFailureDelayOption()) {
            throw new IllegalArgumentException("Attempted to add a FailureElement "
                    + "that is inconsistent with ResourceUnit failure delay option");
        }
        if (myFailureProcesses == null) {
            myFailureProcesses = new ArrayList<>();
            myFailureNoticeQ = new Queue<>(this, getName() + ":FailureQ",
                    myFailureQDiscipline, myFailureQStatOption);
            myEndDownTimeAction = new EndDownTimeAction();
        }
        if (myFailureProcesses.contains(fe)){
            throw new IllegalArgumentException("The supplied FailureElement was already added to the ResourceUnit");
        }
        myFailureProcesses.add(fe);
    }

    /**
     *
     * @return the enterWaitingState discipline of the failures if they can wait
     */
    public final Discipline getFailureQDiscipline() {
        return myFailureQDiscipline;
    }

    /**
     *
     * @return true if statistics are reported on the failure enterWaitingState
     */
    public final boolean getFailureQStatOption() {
        return myFailureQStatOption;
    }

    /**
     *
     * @return true if statistics are reported on the request enterWaitingState
     */
    public final boolean getRequestQStatOption() {
        return myRequestQ.getQueueStatsOption();
    }

    /**
     *
     * @return true if state statistics are collected
     */
    public final boolean getCollectStateStatsOption() {
        return myCollectStateStats;
    }

    /**
     * The default is false
     *
     * @return true means enterWaitingState statistics include statistics on
     * requests
     * that
     * were canceled.
     */
    public final boolean getRequestQCancelStatOption() {
        return myRequestQCancelStatOption;
    }

    /**
     *
     * @return true if statistics are collected on individual requests
     */
    public final boolean getRequestStatisticsOption() {
        return myRequestStatOption;
    }

    /**
     * The request that is currently using the unit
     *
     * @return an Optional
     */
    public final Optional<Request> getCurrentRequest() {
        return Optional.ofNullable(myCurrentRequest);
    }

    /**
     *
     * @return returns true if attached FailureElements that allow failure
     * delays are allowed to be attached to the resource
     */
    public final boolean getFailureDelayOption() {
        return myFailureDelayOption;
    }

    /**
     *
     * @return true if inactive periods caused by a Schedule are allowed to
     * delay before occurring.
     */
    public final boolean getInactivePeriodDelayOption() {
        return myInactivePeriodDelayOption;
    }

    /**
     * The default is false
     *
     * @return true if attached failure elements will start automatically upon
     * initialization
     */
    public final boolean getAutoFailuresFlag() {
        return myAutoStartFailuresFlag;
    }

    @Override
    public String asString(){
        String s = getName() + ": state = " + myCurrentState;
        return (s);
    }

    /**
     *
     * @return true if FailureElements have been added to the unit
     */
    public final boolean hasFailureElements() {
        return myFailureProcesses != null;
    }

    /**
     * Checks if the resource is idle.
     *
     * @return true if idle, false otherwise
     */
    public final boolean isIdle() {
        return (myCurrentState == myIdleState);
    }

    /**
     * Checks to see if the resource is busy
     *
     * @return
     */
    public final boolean isBusy() {
        return (myCurrentState == myBusyState);
    }

    /**
     * Checks if the resource is failed
     *
     * @return true if idle, false otherwise
     */
    public final boolean isFailed() {
        return (myCurrentState == myFailedState);
    }

    /**
     * Checks to see if the resource is inactive
     *
     * @return
     */
    public final boolean isInactive() {
        return (myCurrentState == myInactiveState);
    }

    /**
     * Checks if the resource was idle.
     *
     * @return true if idle, false otherwise
     */
    public final boolean isPreviousStateIdle() {
        return (myPrevState == myIdleState);
    }

    /**
     * Checks to see if the resource was busy
     *
     * @return
     */
    public final boolean isPreviousStateBusy() {
        return (myPrevState == myBusyState);
    }

    /**
     * Checks if the resource was failed
     *
     * @return true if idle, false otherwise
     */
    public final boolean isPreviousStateFailed() {
        return (myPrevState == myFailedState);
    }

    /**
     * Checks to see if the resource was inactive
     *
     * @return
     */
    public final boolean isPreviousStateInactive() {
        return (myPrevState == myInactiveState);
    }

    /**
     * Checks if the preemption rule of the request is compatible
     * with the failure delay option. If the request doesn't allow
     * preemption and failures cannot be delayed, this means that the
     * request will be rejected.
     *
     * @param request the request to check
     * @return true if compatible
     */
    public boolean isPreemptionRuleCompatible(Request request) {
        if (request.getPreemptionRule() == PreemptionRule.NONE) {
            if (getFailureDelayOption() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * The default preemption rule is RESUME. Creates a simple request
     * and seizes the resource for an indefinite amount of time. A convenience
     * method to combine request building and seizing.
     *
     * @param reactor the reactor to use
     * @return
     */
    public final Request seize(RequestReactorIfc reactor) {
        return seize(reactor, null, null);
    }

    /**
     * The default preemption rule is RESUME. Creates a simple request
     * and seizes the resource. A convenience method to combine request building
     * and seizing.
     *
     * @param reactor the reactor to use
     * @param time the time duration for the request
     * @return
     */
    public final Request seize(RequestReactorIfc reactor, double time) {
        return seize(reactor, new Constant(time), null, null);
    }

        /**
     * The default preemption rule is RESUME. Creates a simple request
     * and seizes the resource. A convenience method to combine request building
     * and seizing.
     *
     * @param reactor the reactor to use
     * @param time the time duration for the request
     *  @param entity the object associated with the request
     * @return
     */
    public final Request seize(RequestReactorIfc reactor, double time, Object entity) {
        return seize(reactor, new Constant(time), null, entity);
    }
    
    /**
     * The default preemption rule is RESUME. Creates a simple request
     * and seizes the resource. A convenience method to combine request building
     * and seizing.
     *
     * @param reactor the reactor to use
     * @param timeValueGetter the time duration for the request
     * @return
     */
    public final Request seize(RequestReactorIfc reactor, GetValueIfc timeValueGetter) {
        return seize(reactor, timeValueGetter, null, null);
    }

    /**
     * The default preemption rule is RESUME. Creates a simple request
     * and seizes the resource. A convenience method to combine request building
     * and seizing.
     *
     * @param reactor the reactor to use
     * @param timeValueGetter the time duration for the request
     * @param entity the object associated with the request
     * @return
     */
    public final Request seize(RequestReactorIfc reactor, GetValueIfc timeValueGetter,
            Object entity) {
        return seize(reactor, timeValueGetter, null, entity);
    }

    /**
     * Creates a simple request using Request default values and seizes
     * the resource unit. A convenience method to combine request building
     * and seizing.
     *
     * @param reactor the reactor to use
     * @param timeValueGetter the time duration for the request
     * @param rule the rule
     * @param entity the object associated with the request
     * @return
     */
    public final Request seize(RequestReactorIfc reactor, GetValueIfc timeValueGetter,
            PreemptionRule rule, Object entity) {
        Request request = Request.builder()
                .createTime(getTime())
                .reactor(reactor).entity(entity)
                .rule(rule)
                .duration(timeValueGetter)
                .build();
        return seize(request);
    }

    /**
     * Causes the request to enter the resource. If the resource is idle, the
     * request will be using the resource. If the resource is not idle the
     * request will wait. A Request will be rejected if its preemption rule is
     * NONE and the
     * ResourceUnit's failure delay option is false. This implies that the
     * Request cannot be processed by the ResourceUnit because the request
     * cannot be preempted and the resource unit does not permit its failures to
     * delay (i.e. they must preempt).
     *
     * @param request a request made by this unit
     * @return the request is returned to emphasize that the user may want to
     * check its state
     */
    @Override
    public final Request seize(Request request) {
        //JSL.out.println(getTime() + " > ResourceUnit.seize(Request)");
        if (request == null) {
            throw new IllegalArgumentException("The Request was null");
        }

        if (request.getPreemptionRule() == PreemptionRule.NONE) {
            if (getFailureDelayOption() == false) {
                request.reject(getTime());
                return request;
            }
        }
        // not rejected, proceed with normal processing
        request.makeReady(getTime());
        myNumSeizes = myNumSeizes + 1;
        myCurrentState.seize(request);
        return request;
    }

    /**
     * Causes the resource to cancel the request. The request must have been
     * made by this resource. Called from Request
     *
     * @param request a request made by this unit
     */
    protected final void cancel(Request request) {
        if (request == null) {
            throw new IllegalArgumentException("The Request was null");
        }
        if (request.getResourceUnit().get() != this) {
            throw new IllegalArgumentException("The Request was not made by this ResourceUnit");
        }
        myCurrentState.cancel(request);
    }

    /**
     * Causes the resource to immediately finish the request. The request must
     * have been made by this resource. Called from Request
     *
     * @param request
     */
    protected final void release(Request request) {
        //JSL.out.println(getTime() + " > ResourceUnit.release(Request)");
        if (request == null) {
            throw new IllegalArgumentException("The Request was null");
        }
        if (request.getResourceUnit().get() != this) {
            throw new IllegalArgumentException("The Request was not made by this ResourceUnit");
        }
        myCurrentState.release(request);
    }

    /**
     *
     * @return the number of times that the resource has been seized
     */
    public final int getTotalSeizes() {
        return myNumSeizes;
    }

    /**
     *
     * @return the total time that the resource has been busy
     */
    public final double getTotalTimeBusy() {
        if (isBusy()) {
            myCurrentState.exit(getTime());
            myCurrentState.enter(getTime());
        }
        return myBusyState.getTotalTimeInState();
    }

    /**
     *
     * @return the total time that the resource has been idle
     */
    public final double getTotalTimeIdle() {
        if (isIdle()) {
            myCurrentState.exit(getTime());
            myCurrentState.enter(getTime());
        }
        return myIdleState.getTotalTimeInState();
    }

    public final Queue.Status getRequestQStatus() {
        return myRequestQ.getStatus();
    }

    public final Optional<QueueResponse> getQRequestQResponses() {
        return myRequestQ.getQueueResponses();
    }

    public final List<Request> getUnmodifiableListforRequestQ() {
        return myRequestQ.getUnmodifiableList();
    }

    public final boolean addRequestQListener(QueueListenerIfc<Request> listener) {
        return myRequestQ.addQueueListener(listener);
    }

    public boolean removeRequestQListener(QueueListenerIfc<Request> listener) {
        return myRequestQ.removeQueueListener(listener);
    }

    public final void changeRequestQDiscipline(Discipline discipline) {
        myRequestQ.changeDiscipline(discipline);
    }

    public final Discipline getCurrentRequestQDiscipline() {
        return myRequestQ.getCurrentDiscipline();
    }

    public final void changeRequestQPriority(Request qObject, int priority) {
        myRequestQ.changePriority(qObject, priority);
    }

    public final Discipline getInitialRequestQDiscipline() {
        return myRequestQ.getInitialDiscipline();
    }

    public final void setInitialRequestQDiscipline(Discipline discipline) {
        myRequestQ.setInitialDiscipline(discipline);
    }

    public final int requestQsize() {
        return myRequestQ.size();
    }

    public final boolean isRequestQEmpty() {
        return myRequestQ.isEmpty();
    }

    /**
     *
     * @return the time the idle state was last exited
     */
    public final double getTimeExitedIdle() {
        return myIdleState.getTimeStateExited();
    }

    /**
     *
     * @return the time the idle state was last entered
     */
    public final double getTimeEnteredIdle() {
        return myIdleState.getTimeStateEntered();
    }

    /**
     *
     * @return the time the busy state was last entered
     */
    public final double getTimeEnteredBusy() {
        return myBusyState.getTimeStateEntered();
    }

    /**
     *
     * @return the time the busy state was last exited
     */
    public final double getTimeExitedBusy() {
        return myBusyState.getTimeStateExited();
    }

    /**
     *
     * @return the time the failed state was last entered
     */
    public final double getTimeEnteredFailed() {
        return myFailedState.getTimeStateEntered();
    }

    /**
     *
     * @return the time the failed state was last exited
     */
    public final double getTimeExitedFailed() {
        return myFailedState.getTimeStateExited();
    }

    /**
     *
     * @return the time the inactive state was last entered
     */
    public final double getTimeEnteredInactive() {
        return myInactiveState.getTimeStateEntered();
    }

    /**
     *
     * @return the time the inactive state was last exited
     */
    public final double getTimeExitedInactive() {
        return myInactiveState.getTimeStateExited();
    }

    /**
     *
     * @return number of times the busy state was exited
     */
    public final double getNumTimesBusyExited() {
        return myBusyState.getNumberOfTimesExited();
    }

    /**
     *
     * @return number of times the busy state was entered
     */
    public final double getNumTimesBusyEntered() {
        return myBusyState.getNumberOfTimesEntered();
    }

    /**
     *
     * @return number of times the idle state was exited
     */
    public final double getNumTimesIdleExited() {
        return myIdleState.getNumberOfTimesExited();
    }

    /**
     *
     * @return number of times the idle state was entered
     */
    public final double getNumTimesIdleEntered() {
        return myIdleState.getNumberOfTimesEntered();
    }

    /**
     *
     * @return number of times the failed state was exited
     */
    public final double getNumTimesFailedExited() {
        return myFailedState.getNumberOfTimesExited();
    }

    /**
     *
     * @return number of times the failed state was entered
     */
    public final double getNumTimesFailedEntered() {
        return myFailedState.getNumberOfTimesEntered();
    }

    /**
     *
     * @return number of times the inactive state was exited
     */
    public final double getNumTimesInactiveExited() {
        return myInactiveState.getNumberOfTimesExited();
    }

    /**
     *
     * @return number of times the inactive state was entered
     */
    public final double getNumTimesInactiveEntered() {
        return myInactiveState.getNumberOfTimesEntered();
    }

    /**
     *
     * @return the total time that the resource has been failed
     */
    public final double getTotalTimeFailed() {
        if (isFailed()) {
            myCurrentState.exit(getTime());
            myCurrentState.enter(getTime());
        }
        return myFailedState.getTotalTimeInState();
    }

    /**
     *
     * @return the total time that the resource has been inactive
     */
    public final double getTotalTimeInactive() {
        if (isInactive()) {
            myCurrentState.exit(getTime());
            myCurrentState.enter(getTime());
        }
        return myInactiveState.getTotalTimeInState();
    }

    /**
     *
     * @return the total time that the resource has been idle, busy, failed, or
     * inactive
     */
    public final double getTotalTime() {
        return getTime() - myStartTime;
    }

    /**
     * Called by FailureElement to indicate a failure has occurred
     *
     * @param failure the notice
     */
    void receiveFailureNotice(FailureNotice failure) {
        failure.setResourceUnit(this);
        myCurrentState.fail(failure);
    }

    /**
     * Sets the state of the resource and notifies observers
     *
     * @param state the state
     */
    protected final void setState(ResourceState state) {
        myCurrentState.exit(getTime());
        myPrevState = myCurrentState;
        myCurrentState = state;
        myCurrentState.enter(getTime());
        notifyUpdateObservers();
        //JSL.out.println(getTime() + " > **In ResourceUnit: was " + myPrevState + " now " + myCurrentState);
        notifyFailureElements();
    }

    /**
     *  Called from setState().  Notifies all attached FailureElements of the specifics
     *  of the state change so that they may react to the change in state. The reason
     *  that FailureElements are notified of the state change is to allow them to react
     *  accordingly. For example, if the resource is failed, a failure element might not
     *  accrue time toward failing again.  In other words, the failure mechanism may
     *  depend on the state of the resource
     */
    protected final void notifyFailureElements() {
        if (hasFailureElements()) {
            for (FailureProcess fe : myFailureProcesses) {
                fe.resourceUnitStateChange(this);
            }
        }
    }

    /**
     *
     * @return the current notice if the unit is failed or null
     */
    protected final FailureNotice getCurrentFailureNotice() {
        return myCurrentFailureNotice;
    }

    /**
     * Checks if there something to do after a request is completed. The order
     * of checking is:
     * <p>
     * 1) if there are both pending failures or pending inactive periods process
     * the one that occurred first
     * <p>
     * 2) process pending requests
     * <p>
     * 3) if none of the above go idle
     * <p>
     * Note: there cannot be a preemption pending after a normal request is
     * completed.
     */
    protected void checkForWorkAfterRequestCompletion() {

        if (isFailureNoticeWaiting() && isInactivePeriodPending()) {
            // compare them, whichever has smallest creation time gets processed next
            FailureNotice nextFailure = myFailureNoticeQ.peekNext();
            if (nextFailure.getCreateTime() <= getNextInactivePeriodNotice().getCreateTime()) {
                processNextFailureNotice();
            } else {
                processNextInactiveNotice();
            }
            return;
        }
        // can't be both if here
        // if there is a failure process it
        if (isFailureNoticeWaiting()) {
            processNextFailureNotice();
            return;
        }
        // if there is an pending inactive period then process it
        if (isInactivePeriodPending()) {
            processNextInactiveNotice();
            return;
        }
        // check regular requests
        if (isRequestWaiting()) {
            processNextRequest();
            return;
        }
        // can't transition to failed, inactive, or busy, must be idle
        setState(myIdleState);
    }

    /**
     * Checks if there something to do after a failure notice is completed. Note
     * that failure processing is given preference after a failure has occurred.
     * The order of checking is:
     * <p>
     * 1) process pending failures
     * <p>
     * 2) process pending inactivity
     * <p>
     * 3) process a preemption
     * <p>
     * 4) process pending requests
     * <p>
     * 5) if none of the above go idle
     * <p>
     */
    protected void checkForWorkAfterFailureNoticeCompletion() {
        // first process any failures
        if (isFailureNoticeWaiting()) {
            processNextFailureNotice();
            return;
        }
        // can't transition to failed, since no more failure notices
        // could go from failed to inactive
        // check for inactive notices
        if (isInactivePeriodPending()) {
            processNextInactiveNotice();
            return;
        }
        // can't transition to failed or inactive
        // could go from failed to busy
        // check for requests
        // if there is a preemption then process it   
        if (isPreemptionWaiting()) {
            processPreemption();
            return;
        }
        // nothing preempted, check regular requests
        if (isRequestWaiting()) {
            processNextRequest();
            return;
        }
        // can't transition to failed, inactive, or busy, must be idle
        setState(myIdleState);
    }

    /**
     *
     * @return true if there is a request that was preempted waiting
     */
    public final boolean isPreemptionWaiting() {
        return myPreemptedRequest != null;
    }

    /**
     *
     * @return true if there is a failure notice that needs processing
     */
    public final boolean isFailureNoticeWaiting() {
        if (myFailureNoticeQ == null) {
            return false;
        }
        return myFailureNoticeQ.peekNext() != null;
    }

    /**
     *
     * @return true if there is a request that needs processing
     */
    public final boolean isRequestWaiting() {
        return myRequestQ.peekNext() != null;
    }

    /**
     *
     * @return true if there is a pending inactive period that needs processing
     */
    public final boolean isInactivePeriodPending() {
        return myPendingInactivePeriodNotice != null;
    }

    /**
     * Processes the pending preemption
     * <p>
     */
    protected void processPreemption() {
        if (!isPreemptionWaiting()) {
            throw new IllegalStateException("There was not pending preemption to process.");
        }
        myCurrentRequest = myPreemptedRequest;
        myPreemptedRequest = null;
        setState(myBusyState);
        myCurrentRequest.resume(getTime());
        double time = myCurrentRequest.getTimeRemaining() * myCurrentRequest.getTimeUnits();
        myCurrentRequestEvent = scheduleEvent(myEndRequestUsageAction, time);
    }

    /**
     * Processes the next request
     * <p>
     */
    protected void processNextRequest() {
        if (!isRequestWaiting()) {
            throw new IllegalStateException("There was no request to process.");
        }
        Request next = getNextRequest();
        scheduleEndOfService(next);
    }

    /**
     * Processes the next failure notice
     * <p>
     */
    protected void processNextFailureNotice() {
        if (!isFailureNoticeWaiting()) {
            throw new IllegalStateException("There was no failure notice to process.");
        }
        // get next failure notice
        FailureNotice next = getNextFailureNotice();
        // change state, start the failure and return
        scheduleEndOfFailure(next);
    }

    /**
     * Checks if there are waiting inactive period notices and processes the
     * next one if there is one.
     * <p>
     */
    protected void processNextInactiveNotice() {
        if (!isInactivePeriodPending()) {
            throw new IllegalStateException("There was no pending inactive period to process.");
        }
        InactivePeriodNotice next = getNextInactivePeriodNotice();
        myPendingInactivePeriodNotice = null;
        scheduleEndOfInactivePeriod(next);
    }

    /**
     *
     * @return the next inactive period needing processing or null
     */
    protected InactivePeriodNotice getNextInactivePeriodNotice() {
        return myPendingInactivePeriodNotice;
    }

    /**
     *
     * @return the next failure notice needing processing or null
     */
    protected FailureNotice getNextFailureNotice() {
        if (!hasFailureElements()) {
            return null;
        }
        return myFailureNoticeQ.removeNext();
    }

    /**
     *
     * @return the next request needing processing or null
     */
    protected Request getNextRequest() {
        Request next = myRequestQ.removeNext();
        next.exitWaitingState(myRequestQ, getTime());
        return next;
    }

    /**
     * Collects statistics on requests after they have been completed, rejected,
     * or canceled.
     *
     * @param request
     */
    protected void collectFinalRequestStatistics(Request request) {
        if (getRequestStatisticsOption() == false) {
            return;
        }
        if (request.isCanceled()) {
            myNumCanceled.increment();
        }
        if (request.isRejected()) {
            myNumRejected.increment();
        }
        if (request.isFinished()) {
            myNumCompleted.increment();
            myReqTimeToCompletion.setValue(request.getTimeUntilCompletion());
        }
        if (request.getNumPreemptions() > 0) {
            double n = request.getNumPreemptions();
            myNumPreemptions.increment(n);
            double t = request.getTotalPreemptionTime();
            myTotalPreemptTime.increment(t);
            myPreemptProb.setValue(true);
            myPreemptTime.setValue(t);
        }
    }

    /**
     * Places the unit in the failed state using the supplied failure notice
     *
     * @param failureNotice
     */
    protected void scheduleEndOfFailure(FailureNotice failureNotice) {
        myCurrentFailureNotice = failureNotice;
        myCurrentFailureNotice.activate();
        setState(myFailedState);
        myCurrentDownTimeEvent = scheduleEvent(myEndDownTimeAction,
                myCurrentFailureNotice.getDuration());
    }

    /**
     * Causes the unit to become busy, makes the request the current request
     * that is being processed and schedules the ending of the processing of the
     * request
     *
     * @param request
     */
    protected void scheduleEndOfService(Request request) {
        setState(myBusyState);
        myCurrentRequest = request;
        myCurrentRequest.allocate(this, getTime());
        double time = myCurrentRequest.getTimeRemaining() * myCurrentRequest.getTimeUnits();
        if (Double.isFinite(time)) {
            myCurrentRequestEvent = scheduleEvent(myEndRequestUsageAction, time);
        }
    }

    protected void scheduleEndOfInactivePeriod(InactivePeriodNotice notice) {
        myCurrentInactivePeriodNotice = notice;
        myCurrentInactivePeriodNotice.activate();
        setState(myInactiveState);
        myCurrentInactivePeriodEvent = scheduleEvent(myEndInactivePeriodAction,
                myCurrentInactivePeriodNotice.getInactiveTime());
    }

    /**
     * Causes the request to wait in the request enterWaitingState
     *
     * @param request
     */
    protected void enqueueIncomingRequest(Request request) {
        myRequestQ.enqueue(request);
        request.enterWaitingState(myRequestQ, getTime());
    }

    /**
     * Causes an incoming failure notice to wait
     *
     * @param failureNotice
     */
    protected void enqueueIncomingFailureNotice(FailureNotice failureNotice) {
        failureNotice.delay();
        myFailureNoticeQ.enqueue(failureNotice);
    }

    /**
     * Handles what to do if the resource is told to fail while busy
     *
     * @param failureNotice the failure notice that caused the failure
     */
    protected void failedWhileBusy(FailureNotice failureNotice) {
        if (myCurrentRequest.doesNotAllowPreemption()) {
            if (!failureNotice.isDelayable()) {
                throw new IllegalStateException("Current request cannot be "
                        + "preempted and failure cannot be delayed");
            } else {
                // failure notice should wait until request is completed
                enqueueIncomingFailureNotice(failureNotice);
            }
        } else {
            // request can be preempted, need to handle case
            preemptCurrentRequest();
            scheduleEndOfFailure(failureNotice);
        }
    }

    /**
     * Handles what to do if the resource is told to fail while inactive. The
     * default behavior has the failure delay if it can be delayed; otherwise,
     * it is ignored
     *
     * @param notice
     */
    protected void failedWhileInactive(FailureNotice notice) {
        if (notice.isDelayable()) {
            enqueueIncomingFailureNotice(notice);
        } else {
            notice.ignore();
        }
    }

    /**
     * Handles what to do if the resource is told to be inactive while busy, If
     * the current request can be preempted, it is preempted and the inactive
     * period begins immediately. If the current request cannot be delayed, the
     * incoming request is checked if it can be delayed. If so, it replaces any
     * pending inactive periods and becomes the pending period. In this case the
     * previous pending notice is canceled. If the request and the notice both
     * cannot be delayed, then an exception is thrown.
     *
     * @param notice the inactive period notice
     */
    protected void inactivateWhileBusy(InactivePeriodNotice notice) {
        //in busy state and told to become inactive
        if (myCurrentRequest.doesNotAllowPreemption()) {
            if (!notice.isDelayable()) {
                throw new IllegalStateException("Current request cannot be "
                        + "preempted and the inactive period cannot be delayed");
            } else {
                // inactive period should wait until request is completed
                if (isInactivePeriodPending()) {
                    // there is one waiting already, cancel it
                    myPendingInactivePeriodNotice.cancel();
                }
                // new one becomes pending and is placed in delayed state
                myPendingInactivePeriodNotice = notice;
                myPendingInactivePeriodNotice.delay();
            }
        } else {
            preemptCurrentRequest();
            scheduleEndOfInactivePeriod(notice);
        }
    }

    /**
     * Handles what to do if the resource is told to be inactive while failed.
     * If the new notice can be delayed and if there already is a pending
     * inactive period, the pending one is ignored and the new one becomes the
     * pending period. If the incoming notice cannot be delayed, then it is
     * canceled.
     *
     * @param notice the notice
     */
    protected void inactivateWhileFailed(InactivePeriodNotice notice) {
        if (notice.isDelayable()) {
            if (isInactivePeriodPending()) {
                // there is one waiting already, cancel it
                myPendingInactivePeriodNotice.cancel();
            }
            // new one becomes pending and is placed in delayed state
            myPendingInactivePeriodNotice = notice;
            myPendingInactivePeriodNotice.delay();
        } else {
            notice.cancel();
        }
    }

    /**
     * Handles the arrival of a new inactive period notice, while the resource
     * is already inactive. Since only one inactive period can be waiting at any
     * time, this means that the current period was delayed so much that the
     * next one arrived before the current was able to be completed. The default
     * behavior is to cancel the current period and allow the new period to
     * proceed in full. The state remains inactive.
     *
     * @param notice the notice
     */
    protected void inactivateWhileInactive(InactivePeriodNotice notice) {
        myCurrentInactivePeriodNotice.cancel();
        myCurrentInactivePeriodEvent.setCanceledFlag(true);
        myCurrentInactivePeriodNotice = notice;
        myCurrentInactivePeriodNotice.activate();
        myCurrentInactivePeriodEvent = scheduleEvent(myEndInactivePeriodAction,
                myCurrentInactivePeriodNotice.getInactiveTime());
    }

    /** Called from activateWhenInActive()
     *
     * @param notice the notice
     */
    protected void activateWhenInactive(InactivePeriodNotice notice) {
        notice.complete();
        // first process any failures
        if (isFailureNoticeWaiting()) {
            processNextFailureNotice();
            return;
        }
        // could go from inactive to busy
        // if there is a preemption then process it   
        if (isPreemptionWaiting()) {
            processPreemption();
            return;
        }
        // nothing preempted, check regular requests
        if (isRequestWaiting()) {
            processNextRequest();
            return;
        }
        // can't transition to failed, inactive, or busy, must be idle
        setState(myIdleState);
    }

    /**
     * How to preempt the current request. Called from failedWhileBusy()
     */
    protected void preemptCurrentRequest() {
        PreemptionRule rule = myCurrentRequest.getPreemptionRule();
        switch (rule) {
            case RESUME:
                preemptResumableRequest();
                break;
            case CANCEL:
                preemptCancelableRequest();
                break;
            case RESTART:
                preemptRestartableRequest();
                break;
            default:
                throw new IllegalStateException("No such preempton rule");
        }
    }

    /**
     * Called from preemptCurrentRequest(). How to preempt a request that can be
     * resumed.
     */
    protected void preemptResumableRequest() {
        // need to note preempted request
        myPreemptedRequest = myCurrentRequest;
        myCurrentRequest.preempt(getTime());
        // cancel the event of current request
        if (myCurrentRequestEvent != null) {
            myCurrentRequestEvent.setCanceledFlag(true);
        }
        myCurrentRequestEvent = null;
        myCurrentRequest = null;
    }

    /**
     * Called from preemptCurrentRequest(). How to preempt a request that must
     * be canceled.
     */
    protected void preemptCancelableRequest() {
        myCurrentRequest.preempt(getTime());
        if (myCurrentRequestEvent != null) {
            myCurrentRequestEvent.setCanceledFlag(true);
        }
        myCurrentRequestEvent = null;
        myCurrentRequest = null;
    }

    /**
     * Called from preemptCurrentRequest(). How to preempt a request that must
     * be restarted.
     */
    protected void preemptRestartableRequest() {
        // need to note preempted request
        myPreemptedRequest = myCurrentRequest;
        myCurrentRequest.preempt(getTime());
        // cancel the event of current request
        if (myCurrentRequestEvent != null) {
            myCurrentRequestEvent.setCanceledFlag(true);
        }
        myCurrentRequestEvent = null;
        myCurrentRequest = null;
    }

    /**
     * Tells the resource to listen and react to changes in the supplied
     * Schedule. Any scheduled items on the schedule will be interpreted as
     * changes to make the resource become inactive. The resource can only
     * listen to one Schedule.  An IllegalArgumentException is thrown if
     * this resource unit is already using a schedule (i.e. isUsingSchedule() is true).
     *
     * @param schedule the schedule to use, must not be null
     * @return the ScheduleChangeListenerIfc for the ResourceUnit
     */
    public final ScheduleChangeListenerIfc useSchedule(Schedule schedule) {
        if (schedule == null){
            throw new IllegalArgumentException("The supplied Schedule was null");
        }
        if (isUsingSchedule()) {
            throw new IllegalArgumentException("The ResourceUnit is already listening to a Schedule");
        }
        myScheduleListener = new ScheduleListener();
        schedule.addScheduleChangeListener(myScheduleListener);
        return myScheduleListener;
    }

    /**
     *
     * @return true if already listening to a Schedule
     */
    public final boolean isUsingSchedule() {
        return myScheduleListener != null;
    }

    /**
     * Called from scheduledItemStarted() when a Schedule notifies of an item (period) beginning
     *
     * @param notice
     */
    protected void receiveInactivePeriodNotice(InactivePeriodNotice notice) {
        myCurrentState.inactivate(notice);
    }

    protected class ResourceState extends State {

        protected ResourceState(String name) {
            super(name);
        }

        protected void seize(Request request) {
            throw new IllegalStateException("Request ID = " + request.getId()
                    + " Tried to seize " + ResourceUnit.this.getName()
                    + " from an illegal state: " + myName);
        }

        protected void release(Request request) {
            throw new IllegalStateException("Request ID = " + request.getId()
                    + " Tried to release " + ResourceUnit.this.getName()
                    + " from an illegal state: " + myName);
        }

        protected void completeRequest(Request request) {
            throw new IllegalStateException("Request ID = " + request.getId()
                    + " Tried to release " + ResourceUnit.this.getName()
                    + " from an illegal state: " + myName);
        }

        protected void cancel(Request request) {
            throw new IllegalStateException("Request ID = " + request.getId()
                    + " Tried to cancel using  " + ResourceUnit.this.getName()
                    + " from an illegal state: " + myName);

        }

        protected void fail(FailureNotice failure) {
            throw new IllegalStateException("Failure ID = " + failure.getId()
                    + " Tried to fail " + ResourceUnit.this.getName()
                    + " from an illegal state: " + myName);
        }

        protected void endFailure(FailureNotice failure) {
            throw new IllegalStateException("Failure ID = " + failure.getId()
                    + " Tried to end failure " + ResourceUnit.this.getName()
                    + " from an illegal state: " + myName);
        }

        protected void inactivate(InactivePeriodNotice notice) {
            throw new IllegalStateException(ResourceUnit.this.getName()
                    + " Tried to inactive from an illegal state: " + myName);
        }

        protected void activate(InactivePeriodNotice notice) {
            throw new IllegalStateException(ResourceUnit.this.getName()
                    + " Tried to activate from an illegal state: " + myName);
        }

        @Override
        public final String toString() {
            return (myName);
        }
    }

    protected class Idle extends ResourceState {

        protected Idle() {
            super("Idle");
        }

        @Override
        protected void seize(Request request) {
            // going from idle to busy
            enqueueIncomingRequest(request);
            processNextRequest();
        }

        @Override
        protected void fail(FailureNotice failure) {
            scheduleEndOfFailure(failure);
        }

        @Override
        protected void inactivate(InactivePeriodNotice notice) {
            scheduleEndOfInactivePeriod(notice);
        }

    }

    protected class Busy extends ResourceState {

        protected Busy() {
            super("Busy");
        }

        @Override
        protected void seize(Request request) {
            // trying to seize a busy resource, resource stays busy
            // request has to wait in the request enterWaitingState
            enqueueIncomingRequest(request);
        }

        @Override
        protected void release(Request request) {
            //releasing the request from the outside
            if (request != myCurrentRequest) {
                throw new IllegalStateException("The request attempting "
                        + "to release is not the current busy request of the resource.");
            }
            // must be current request, cancel the event
            if (myCurrentRequestEvent != null) {
                myCurrentRequestEvent.setCanceledFlag(true);
            }
            // act like request was completed naturally
            completeRequest(request);
        }

        @Override
        protected void cancel(Request request) {
            if (request != myCurrentRequest) {
                if (myRequestQ.contains(request)) {
                    myRequestQ.remove(request, getRequestQCancelStatOption());
                    request.exitWaitingState(myRequestQ, getTime());
                }
                request.becomeCanceled(getTime());
            } else {

                // request is current request, cancel the event
                if (myCurrentRequestEvent != null) {
                    myCurrentRequestEvent.setCanceledFlag(true);
                }
                myCurrentRequestEvent = null;
                myCurrentRequest = null;
                // stopped working on current request, need to check for work
                checkForWorkAfterRequestCompletion();
                request.becomeCanceled(getTime());
            }
        }

        @Override
        protected void completeRequest(Request request) {
            // in busy state and told to finish the request
            // releasing a busy resource
            //setState(myIdleState); //TODO NOT NEEDED because of check?
            myCurrentRequest = null;
            myCurrentRequestEvent = null;
            checkForWorkAfterRequestCompletion();
            request.finish(getTime());
        }

        @Override
        protected void inactivate(InactivePeriodNotice notice) {
            inactivateWhileBusy(notice);
        }

        @Override
        protected void fail(FailureNotice failure) {
            failedWhileBusy(failure);
        }

    }

    protected class Failed extends ResourceState {

        protected Failed() {
            super("Failed");
        }

        @Override
        protected void seize(Request request) {
            // trying to seize a failed resource, resource stays failed
            // request has to wait in the request enterWaitingState
            enqueueIncomingRequest(request);
        }

        @Override
        protected void fail(FailureNotice failure) {
            //in failed state and told to fail
            // already failed, stay failed and enterWaitingState the notice
            enqueueIncomingFailureNotice(failure);
        }

        @Override
        protected void endFailure(FailureNotice failure) {
            failure.complete();
            checkForWorkAfterFailureNoticeCompletion();
        }

        @Override
        protected void cancel(Request request) {
            // trying to cancel a request while in the failed state
            if (request == myPreemptedRequest) {
                myPreemptedRequest = null;
            } else if (myRequestQ.contains(request)) {
                myRequestQ.remove(request, getRequestQCancelStatOption());
                request.exitWaitingState(myRequestQ, getTime());
            }
            request.becomeCanceled(myStartTime);
        }

        @Override
        protected void inactivate(InactivePeriodNotice notice) {
            inactivateWhileFailed(notice);
        }
    }

    protected class Inactive extends ResourceState {

        protected Inactive() {
            super("Inactive");
        }

        @Override
        protected void seize(Request request) {
            // trying to seize an inactive resource, resource stays inactive
            // request has to wait in the request enterWaitingState
            enqueueIncomingRequest(request);
        }

        @Override
        protected void cancel(Request request) {
            // trying to cancel a request while in the inactive state
            if (request == myPreemptedRequest) {
                myPreemptedRequest = null;
            } else if (myRequestQ.contains(request)) {
                myRequestQ.remove(request, getRequestQCancelStatOption());
                request.exitWaitingState(myRequestQ, getTime());
            }
            request.becomeCanceled(myStartTime);
        }

        @Override
        protected void fail(FailureNotice failure) {
            failedWhileInactive(failure);
        }

        @Override
        protected void inactivate(InactivePeriodNotice notice) {
            inactivateWhileInactive(notice);
        }

        @Override
        protected void activate(InactivePeriodNotice notice) {
            activateWhenInactive(notice);
        }
    }

    protected class EndRequestUsageAction implements EventActionIfc {

        @Override
        public void action(JSLEvent evt) {
            //the request is finished with the resource
            //JSL.out.println(getTime() + " > ResourceUnit.EndRequestUsageAction.action(JSLEvent)");
            myCurrentState.completeRequest(myCurrentRequest);
        }
    }

    protected class EndDownTimeAction implements EventActionIfc {

        @Override
        public void action(JSLEvent evt) {
            // the failure notice has completed its downtime
            myCurrentState.endFailure(myCurrentFailureNotice);
        }
    }

    protected class EndInactivePeriodAction implements EventActionIfc {

        @Override
        public void action(JSLEvent evt) {
            myCurrentState.activate(myCurrentInactivePeriodNotice);
        }

    }

    protected class ScheduleListener implements ScheduleChangeListenerIfc {

        @Override
        public void scheduleStarted(Schedule schedule) {
            // nothing to do when the schedule starts
        }

        @Override
        public void scheduleEnded(Schedule schedule) {
            // nothing to do when the schedule ends
        }

        @Override
        public void scheduleItemStarted(Schedule.ScheduleItem item) {
            InactivePeriodNotice n = new InactivePeriodNotice(getTime(),
                    item.getDuration(), getInactivePeriodDelayOption());
            receiveInactivePeriodNotice(n);
        }

        @Override
        public void scheduleItemEnded(Schedule.ScheduleItem item) {
            // nothing to do when the item ends
        }

    }
}
