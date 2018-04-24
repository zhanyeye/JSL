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
package jsl.observers.textfile;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import jsl.modeling.*;

import jsl.modeling.elements.variable.Counter;
import jsl.modeling.elements.variable.ResponseVariable;
import jsl.observers.ObserverIfc;
import jsl.utilities.statistic.StatisticAccessorIfc;
import jsl.utilities.reporting.TextReport;

public class MReplicationReport extends TextReport implements ObserverIfc {

    protected Collection<ResponseVariable> myResponseVariables;

    protected Model myModel;

    public MReplicationReport(String name) {
        this(null, name);
    }

    public MReplicationReport(String directory, String name) {
        super(directory, name);
        myResponseVariables = new ArrayList<ResponseVariable>();
    }

    public void update(Object observable, Object obj) {
        String s;
        myModel = (Model) observable;

        if (myModel.checkForBeforeExperiment()) {
            addFileNameAndDate();
            println(myModel);
            myModel.getResponseVariables(myResponseVariables);
        }

        if (myModel.checkForBeforeReplication()) {
            ExperimentGetIfc e = myModel.getExperiment();
            s = "Starting replication " + e.getCurrentReplicationNumber() + "\n";
            s = s + "Planned replication length " + e.getLengthOfReplication() + "\n";
            s = s + "Warmup time " + e.getLengthOfWarmUp() + "\n";
            println(s);
        }

        if (myModel.checkForAfterReplication()) {
            ExperimentGetIfc e = myModel.getExperiment();
            s = "Ending replication " + e.getCurrentReplicationNumber() + "\n";
            s = s + "Ending time " + myModel.getTime() + "\n";

            println(s);

            List<ResponseVariable> rvs = myModel.getResponseVariables();

            if (!rvs.isEmpty()) {
                println();
                println();
                println("----------------------------------------------------------");
                println("Within Replication statistics:");
                println("----------------------------------------------------------");
                println();

                for (ResponseVariable rv : rvs) {
                    if (rv.getDefaultReportingOption()) {
                        println(rv.getWithinReplicationStatistic());
                    }
                }
            }

            List<Counter> counters = myModel.getCounters();

            if (!counters.isEmpty()) {
                println();
                println();
                println("----------------------------------------------------------");
                println("Counter statistics:");
                println("----------------------------------------------------------");
                println();

                for (Counter c : counters) {
                    if (c.getDefaultReportingOption()) {
                        s = "Name " + c.getName() + "\n";
                        s = s + "Final Counter Value " + c.getValue() + "\n";
                        println(s);
                    }
                }

                for (Counter c : counters) {
                    if (c.getDefaultReportingOption()) {
                        ResponseVariable acrossIntervalResponse = c.getAcrossIntervalResponse();
                        if (acrossIntervalResponse != null){
                            println(acrossIntervalResponse.getAcrossReplicationStatistic());
                        }
                    }
                }
            }

        }
    }
}

