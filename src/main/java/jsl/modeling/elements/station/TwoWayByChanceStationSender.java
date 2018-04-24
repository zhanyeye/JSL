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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsl.modeling.elements.station;

import jsl.modeling.ModelElement;
import jsl.modeling.queue.QObject;

/** This station will receive a QObject and immediately
 *  send it out to one of two randomly selected receivers
 * 
 * @author rossetti
 */
public class TwoWayByChanceStationSender extends Station {

    protected TwoWayByChanceQObjectSender myTwoWaySender;
    
    public TwoWayByChanceStationSender(ModelElement parent, double p,
            ReceiveQObjectIfc r1, ReceiveQObjectIfc r2) {
        this(parent, null, p, r1, r2);
    }

    public TwoWayByChanceStationSender(ModelElement parent, String name, double p,
            ReceiveQObjectIfc r1, ReceiveQObjectIfc r2) {
        super(parent, name);
        myTwoWaySender = new TwoWayByChanceQObjectSender(this, p, r1, r2);
        setSender(myTwoWaySender);
    }

    public final void setSecondReceiver(ReceiveQObjectIfc r2) {
        myTwoWaySender.setSecondReceiver(r2);
    }

    public final void setFirstReceiver(ReceiveQObjectIfc r1) {
        myTwoWaySender.setFirstReceiver(r1);
    }

    @Override
    public void receive(QObject qObj) {
        send(qObj);
    }
}
