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
package test.misc;

import ex.queueing.DriverLicenseBureauWithQ;
import jsl.modeling.ExperimentGetIfc;
import jsl.modeling.Model;
import jsl.modeling.Simulation;
import jsl.modeling.queue.QueueResponse;
import jsl.utilities.math.JSLMath;
import jsl.utilities.random.distributions.Exponential;
import jsl.utilities.statistic.StatisticAccessorIfc;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author rossetti
 */
public class ModelElementTesting {

    private Simulation mySim;

    private ExperimentGetIfc myExp;

    private Model myModel;

    DriverLicenseBureauWithQ myDLB;

    @Before
    public void setUp() {

        mySim = new Simulation();

        myExp = mySim.getExperiment();
        myModel = mySim.getModel();
        // create the model element and attach it to the main model
        myDLB = new DriverLicenseBureauWithQ(myModel);

        // set the parameters of the experiment
//        mySim.setNumberOfReplications(30);
//        mySim.setLengthOfReplication(200000.0);
//        mySim.setLengthOfWarmUp(50000.0);
//        mySim.setAdvanceStreamNumber(5);
    }

    @Test
    public void test1() {
        StringBuilder sb = new StringBuilder();
        myModel.getInitializationOrderAsString(sb);
        System.out.println(sb.toString());
        assertTrue(true);
    }

    @Test
    public void test2(){
        System.out.println(myModel.getModelElementsAsString());
    }

}
