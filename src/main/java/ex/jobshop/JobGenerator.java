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
package ex.jobshop;

import java.util.Iterator;

import jsl.modeling.JSLEvent;
import jsl.modeling.ModelElement;
import jsl.modeling.elements.EventGenerator;
import jsl.modeling.elements.RandomElement;
import jsl.modeling.queue.QObject;
import jsl.modeling.elements.variable.ResponseVariable;
import jsl.utilities.random.RandomIfc;

/**
 * @author rossetti
 *
 */
public class JobGenerator extends EventGenerator {

    protected RandomElement<JobType> myJobTypes;
    
    //protected DEmpiricalList<JobType> myJobTypes;

    /**
     * @param parent
     */
    public JobGenerator(ModelElement parent) {
        this(parent, null, null, Long.MAX_VALUE, Double.POSITIVE_INFINITY, null);
    }

    /**
     * @param parent
     * @param name
     */
    public JobGenerator(ModelElement parent, String name) {
        this(parent, null, null, Long.MAX_VALUE, Double.POSITIVE_INFINITY, name);
    }

    /**
     * @param parent
     * @param timeUntilFirst
     */
    public JobGenerator(ModelElement parent, RandomIfc timeUntilFirst) {
        this(parent, timeUntilFirst, null, Long.MAX_VALUE, Double.POSITIVE_INFINITY, null);
    }

    /**
     * @param parent
     * @param timeUntilFirst
     * @param timeUntilNext
     */
    public JobGenerator(ModelElement parent, RandomIfc timeUntilFirst,
            RandomIfc timeUntilNext) {
        this(parent, timeUntilFirst, timeUntilNext, Long.MAX_VALUE, Double.POSITIVE_INFINITY, null);
    }

    /**
     * @param parent
     * @param timeUntilFirst
     * @param timeUntilNext
     * @param name
     */
    public JobGenerator(ModelElement parent, RandomIfc timeUntilFirst,
            RandomIfc timeUntilNext, String name) {
        this(parent, timeUntilFirst, timeUntilNext, Long.MAX_VALUE, Double.POSITIVE_INFINITY, name);
    }

    /**
     * @param parent
     * @param timeUntilFirst
     * @param timeUntilNext
     * @param maxNum
     */
    public JobGenerator(ModelElement parent, RandomIfc timeUntilFirst,
            RandomIfc timeUntilNext, Long maxNum) {
        this(parent, timeUntilFirst, timeUntilNext, maxNum, Double.POSITIVE_INFINITY, null);
    }

    /**
     * @param parent
     * @param timeUntilFirst
     * @param timeUntilNext
     * @param maxNum
     * @param timeUntilLast
     */
    public JobGenerator(ModelElement parent, RandomIfc timeUntilFirst,
            RandomIfc timeUntilNext, Long maxNum,
            double timeUntilLast) {
        this(parent, timeUntilFirst, timeUntilNext, maxNum, timeUntilLast, null);
    }

    /**
     * @param parent
     * @param timeUntilFirst
     * @param timeUntilNext
     * @param maxNum
     * @param timeUntilLast
     * @param name
     */
    public JobGenerator(ModelElement parent, RandomIfc timeUntilFirst,
            RandomIfc timeUntilNext, Long maxNum,
            double timeUntilLast, String name) {
        super(parent, null, timeUntilFirst, timeUntilNext, maxNum, timeUntilLast, name);
        //myJobTypes = new DEmpiricalList<JobType>();
        myJobTypes = new RandomElement<JobType>(this);
    }

    public void addJobType(String name, Sequence sequence, double prob) {
        ResponseVariable v = new ResponseVariable(this, name + "SystemTime");
        JobType type = new JobType();
        type.myName = name;
        type.mySequence = sequence;
        type.mySystemTime = v;
        myJobTypes.add(type, prob);
    }

    public void addLastJobType(String name, Sequence sequence) {
        ResponseVariable v = new ResponseVariable(this, name + "SystemTime");
        JobType type = new JobType();
        type.myName = name;
        type.mySequence = sequence;
        type.mySystemTime = v;
        myJobTypes.addLast(type);
    }

    @Override
    protected void generate(JSLEvent event) {
        if (!myJobTypes.isEmpty()) {
            // create the job
            Job job = new Job(getTime());
            // tell it to start its sequence
            job.doNextJobStep();
        }
    }

    public class JobType {

        String myName;

        Sequence mySequence;

        ResponseVariable mySystemTime;
    }

    public class Job extends QObject {

        JobType myType;

        Iterator<JobStep> myProcessPlan;

        double myServiceTime;

        Job(double time) {
            super(time);
            myType = myJobTypes.getRandomElement();
            myProcessPlan = myType.mySequence.getIterator();
            setName(myType.myName);
        }

        public void doNextJobStep() {

            if (myProcessPlan.hasNext()) {
                JobStep step = myProcessPlan.next();
                myServiceTime = step.getProcessingTime();
                WorkStation w = step.getWorkStation();
                w.arrive(this);
            } else {
                myType.mySystemTime.setValue(getTime() - getCreateTime());
            }
        }

        public double getServiceTime() {
            return (myServiceTime);
        }
    }
}
