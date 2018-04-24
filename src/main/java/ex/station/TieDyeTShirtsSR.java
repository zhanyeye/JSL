/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ex.station;

import java.util.ArrayList;
import java.util.List;
import jsl.modeling.JSLEvent;
import jsl.modeling.Model;
import jsl.modeling.ModelElement;
import jsl.modeling.Simulation;
import jsl.modeling.SimulationReporter;
import jsl.modeling.elements.EventGenerator;
import jsl.modeling.queue.QObject;
import jsl.modeling.elements.station.ReceiveQObjectIfc;
import jsl.modeling.elements.station.SResource;
import jsl.modeling.elements.station.SingleQueueStation;
import jsl.modeling.elements.variable.RandomVariable;
import jsl.modeling.elements.variable.ResponseVariable;
import jsl.modeling.elements.variable.TimeWeighted;
import jsl.utilities.random.distributions.DEmpiricalCDF;
import jsl.utilities.random.distributions.Exponential;
import jsl.utilities.random.distributions.Triangular;
import jsl.utilities.random.distributions.Uniform;
import jsl.modeling.elements.EventGeneratorActionIfc;

/**
 *
 * @author rossetti
 */
public class TieDyeTShirtsSR extends ModelElement {

    private final EventGenerator myOrderGenerator;
    private RandomVariable myTBOrders;
    private RandomVariable myOrderSize;
    private RandomVariable myOrderType;
    private RandomVariable myShirtMakingTime;
    private RandomVariable myPaperWorkTime;
    private RandomVariable myPackagingTime;
    private SingleQueueStation myShirtMakingStation;
    private SingleQueueStation myPWStation;
    private SingleQueueStation myPackagingStation;
    private SResource myShirtMakers;
    private SResource myPackager;
    private ResponseVariable mySystemTime;
    private TimeWeighted myNumInSystem;

    public TieDyeTShirtsSR(ModelElement parent) {
        this(parent, null);
    }

    public TieDyeTShirtsSR(ModelElement parent, String name) {
        super(parent, name);
        myTBOrders = new RandomVariable(this, new Exponential(60));
        myOrderGenerator = new EventGenerator(this, new OrderArrivals(), myTBOrders, myTBOrders);
        double[] a = {1, 0.7, 2, 1.0};
        double[] b = {3, 0.75, 5, 1.0};
        DEmpiricalCDF type = new DEmpiricalCDF(a);
        DEmpiricalCDF size = new DEmpiricalCDF(b);
        myOrderSize = new RandomVariable(this, size);
        myOrderType = new RandomVariable(this, type);
        myShirtMakingTime = new RandomVariable(this, new Uniform(15, 25));
        myPaperWorkTime = new RandomVariable(this, new Uniform(8, 10));
        myPackagingTime = new RandomVariable(this, new Triangular(5, 10, 15));
        myShirtMakers = new SResource(this, 2, "ShirtMakers_R");
        myPackager = new SResource(this, 1, "Packager_R");
        myShirtMakingStation = new SingleQueueStation(this, myShirtMakers,
                myShirtMakingTime, "Shirt_Station");
        myPWStation = new SingleQueueStation(this, myPackager,
                myPaperWorkTime, "PW_Station");
        myPackagingStation = new SingleQueueStation(this, myPackager,
                myPackagingTime, "Packing_Station");
        // need to set senders/receivers
        myShirtMakingStation.setNextReceiver(new AfterShirtMaking());
        myPWStation.setNextReceiver(new AfterPaperWork());
        myPackagingStation.setNextReceiver(new Dispose());
        mySystemTime = new ResponseVariable(this, "System Time");
        myNumInSystem = new TimeWeighted(this, "Num in System");
    }

    private class OrderArrivals implements EventGeneratorActionIfc {

        @Override
        public void generate(EventGenerator generator, JSLEvent event) {
            myNumInSystem.increment();
            Order order = new Order();
            List<Order.Shirt> shirts = order.getShirts();
            
            for(Order.Shirt shirt: shirts){
                myShirtMakingStation.receive(shirt);
            }
            myPWStation.receive(order.getPaperWork());

        }

    }

    protected class AfterShirtMaking implements ReceiveQObjectIfc {

        @Override
        public void receive(QObject qObj) {
            Order.Shirt shirt = (Order.Shirt) qObj;
            shirt.setDoneFlag();
        }

    }

    protected class AfterPaperWork implements ReceiveQObjectIfc {

        @Override
        public void receive(QObject qObj) {
            Order.PaperWork pw = (Order.PaperWork) qObj;
            pw.setDoneFlag();
        }

    }

    protected class Dispose implements ReceiveQObjectIfc {

        @Override
        public void receive(QObject qObj) {
            // collect final statistics
            myNumInSystem.decrement();
            mySystemTime.setValue(getTime() - qObj.getCreateTime());
            Order o = (Order) qObj;
            o.dispose();
        }

    }

    /**
     * Handles the completion of an order
     *
     */
    private void orderCompleted(Order order) {
        myPackagingStation.receive(order);
    }

    private class Order extends QObject {

        private int myType;
        private int mySize;
        private PaperWork myPaperWork;
        private List<Shirt> myShirts;
        private int myNumCompleted;
        private boolean myPaperWorkDone;

        public Order() {
            this(getTime());
            this.myNumCompleted = 0;
            this.myPaperWorkDone = false;
        }

        public Order(double creationTime) {
            this(creationTime, null);
            this.myNumCompleted = 0;
            this.myPaperWorkDone = false;
        }

        public Order(double creationTime, String name) {
            super(creationTime, name);
            myNumCompleted = 0;
            myPaperWorkDone = false;
            myType = (int) myOrderType.getValue();
            mySize = (int) myOrderSize.getValue();
            myShirts = new ArrayList<>();
            for (int i = 1; i <= mySize; i++) {
                myShirts.add(new Shirt());
            }
            myPaperWork = new PaperWork();
        }

        public int getType() {
            return myType;
        }

        public void dispose() {
            myPaperWork = null;
            myShirts.clear();
            myShirts = null;
        }

        public List<Shirt> getShirts() {
            List<Shirt> list = new ArrayList<>(myShirts);
            return list;
        }

        public PaperWork getPaperWork() {
            return myPaperWork;
        }

        /**
         * The order is complete if it has all its shirts and its paperwork
         *
         * @return
         */
        public boolean isComplete() {
            return ((areShirtsDone()) && (isPaperWorkDone()));
        }

        public boolean areShirtsDone() {
            return (myNumCompleted == mySize);
        }

        public boolean isPaperWorkDone() {
            return (myPaperWorkDone);
        }

        public int getNumShirtsCompleted() {
            return myNumCompleted;
        }

        private void shirtCompleted() {
            if (areShirtsDone()) {
                throw new IllegalStateException("The order already has all its shirts.");
            }
            // okay not complete, need to add shirt
            myNumCompleted = myNumCompleted + 1;
            if (isComplete()) {
                TieDyeTShirtsSR.this.orderCompleted(this);
            }
        }

        private void paperWorkCompleted() {
            if (isPaperWorkDone()) {
                throw new IllegalStateException("The order already has paperwork.");
            }
            myPaperWorkDone = true;
            if (isComplete()) {
                TieDyeTShirtsSR.this.orderCompleted(this);
            }
        }

        protected class Shirt extends QObject {

            protected boolean myDoneFlag = false;

            public Shirt() {
                this(getTime());
            }

            public Shirt(double creationTime) {
                this(creationTime, null);
            }

            public Shirt(double creationTime, String name) {
                super(creationTime, name);
            }

            public Order getOrder() {
                return Order.this;
            }

            public void setDoneFlag() {
                if (myDoneFlag == true) {
                    throw new IllegalStateException("The shirt is already done.");
                }
                myDoneFlag = true;
                Order.this.shirtCompleted();
            }

            public boolean isCompleted() {
                return myDoneFlag;
            }

        }

        protected class PaperWork extends Shirt {

            @Override
            public void setDoneFlag() {
                if (myDoneFlag == true) {
                    throw new IllegalStateException("The paperwork is already done.");
                }
                myDoneFlag = true;
                Order.this.paperWorkCompleted();
            }
        }
    }

        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Simulation sim = new Simulation("Tie-Dye T-Shirts");
        // get the model
        Model m = sim.getModel();
        // add system to the main model
        TieDyeTShirtsSR system = new TieDyeTShirtsSR(m);
        // set the parameters of the experiment
        sim.setNumberOfReplications(50);
        //sim.setNumberOfReplications(2);
        sim.setLengthOfReplication(200000.0);
        sim.setLengthOfWarmUp(50000.0);
        SimulationReporter r = sim.makeSimulationReporter();
        System.out.println("Simulation started.");
        sim.run();
        System.out.println("Simulation completed.");
        r.printAcrossReplicationSummaryStatistics();
    }
}
