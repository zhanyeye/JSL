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
package ex.station;

import jsl.modeling.JSLEvent;
import jsl.modeling.ModelElement;
import jsl.modeling.SchedulingElement;
import jsl.modeling.Simulation;
import jsl.modeling.SimulationReporter;
import jsl.modeling.elements.EventGenerator;
import jsl.modeling.queue.QObject;
import jsl.modeling.elements.station.SendQObjectIfc;
import jsl.modeling.elements.station.SingleQueueStation;
import jsl.modeling.elements.station.TwoWayByChanceStationSender;
import jsl.modeling.elements.variable.RandomVariable;
import jsl.utilities.random.distributions.Exponential;
import jsl.modeling.elements.EventGeneratorActionIfc;

/**
 * Arriving customers choose randomly between two stations.  
 * The arrivals are Poisson with mean rate 1.1. Thus, the time 
 * between arrivals is exponential with mean 1/1.1.
 * The first station is chosen with probability 0.4. The second
 * station is chosen with probability 0.6.
 * The service times of the stations are exponential with means 0.8 and 0.7, 
 * respectively. After receiving service at the chosen station, the
 * customer leaves.
 *
 * @author rossetti
 */
public class ChooseBetweenTwoStations extends SchedulingElement {

    protected EventGenerator myArrivalGenerator;

    protected RandomVariable myTBA;

    protected SingleQueueStation myStation1;

    protected SingleQueueStation myStation2;

    protected RandomVariable myST1;

    protected RandomVariable myST2;
    
    protected TwoWayByChanceStationSender myTwoWay;

    public ChooseBetweenTwoStations(ModelElement parent) {
        this(parent, null);
    }

    public ChooseBetweenTwoStations(ModelElement parent, String name) {
        super(parent, name);

        myTBA = new RandomVariable(this, new Exponential(1.0/1.1));

        myST1 = new RandomVariable(this, new Exponential(0.8));

        myST2 = new RandomVariable(this, new Exponential(0.7));

        myArrivalGenerator = new EventGenerator(this, new Arrivals(), myTBA, myTBA);

        // Stations must have a sender or a receiver
        Dispose d = new Dispose();
        
        myStation1 = new SingleQueueStation(this, myST1, d, "Station1");

        myStation2 = new SingleQueueStation(this, myST2, d, "Station2");
        
        myTwoWay = new TwoWayByChanceStationSender(this, 0.4, myStation1, myStation2);

    }

    protected class Arrivals implements EventGeneratorActionIfc {

        @Override
        public void generate(EventGenerator generator, JSLEvent event) {
            
            myTwoWay.receive(new QObject(getTime()));
        }

    }

    protected class Dispose implements SendQObjectIfc {

        @Override
        public void send(QObject qObj) {
            // do nothing
        }
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Simulation s = new Simulation("Choose btw 2 Stations Example");
        
        new ChooseBetweenTwoStations(s.getModel());
        
        s.setNumberOfReplications(10);
        s.setLengthOfReplication(20000);
        s.setLengthOfWarmUp(5000);
        SimulationReporter r = s.makeSimulationReporter();
        
        s.run();
        
        r.printAcrossReplicationSummaryStatistics();
    }

}
