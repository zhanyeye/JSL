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
package jsl.modeling.elements.variable;

import jsl.modeling.ModelElement;
import jsl.modeling.elements.RandomElementIfc;
import jsl.utilities.random.RandomIfc;
import jsl.utilities.reporting.JSL;

/** A random variable (RandomVariable) is a function that maps a probability space to a real number.
 *  A random variable uses a RandomIfc to provide the underlying mapping to a real number via the getValue() method.
 *  
 *  To construct a RandomVariable the user must provide an instance of a class that implements the RandomIfc interface as the initial random source.
 *  This source is used to initialize the source of randomness for each replication.
 *  
 *  WARNING:  For efficiency, this class uses a direct reference to the supplied initial random source.
 *  It simply wraps the supplied object reference to a random source so that it can be utilized within
 *  the JSL model.  Because of the direct reference to the random source, a change to the state of the
 *  random source will be reflected in the use of that instance within this class.  Thus, mutating
 *  the state of the  random source will also see those mutations reflected in the usage of this
 *  class.  This may or not be what is expected by the client.  For example, mutating the state of
 *  the initial random source during a replication may cause each replication to start with different initial
 *  conditions.
 *
 *  Using the setRandomSource() method allows the user to change the source of randomness during a replication.
 *  The source of randomness during a replication is set to the reference of the initial
 *  random source at the beginning of each replication.  This ensures that each replication uses
 *  the same random source during the replication. However, the user may call the setRandomSource() method
 *  to immediately change the source of randomness during the replication.  This change is in effect only during
 *  the current replication.  At the beginning of each replication, the source of randomness is set to
 *  the reference to the initial random source.  This ensures that each replication uses the same random source.
 *  For this reason, the use of setInitialRandomSource() should be limited to before or after
 *  running a simulation experiment.
 *  
 *  The initial source is used to set up the source used during the replication.  If the
 *  client changes the reference to the initial source, this change does not become effective
 *  until the beginning of the next replication.  In other words, the random source used
 *  during the replication is unaffected. However, the client might mutate the initial random source
 *  during a replication.  If this occurs, those changes are immediately reflected within the current replication
 *  and all subsequent replications.  Again, mutating the initial random source during a replication is
 *  generally a bad idea unless you really know what you are doing.
 *
 *  Changing the initial random source between experiments is very common.  For example, to set up an experiment
 *  that has different random characteristics the client can and should change the source of randomness
 *  (either by mutating the random source or by supplying a reference to a different random source.
 *  
 */
public class RandomVariable extends ModelElement implements RandomIfc, RandomElementIfc {

    /** indicates whether or not the random variable's
     *  distribution has it stream reset to the default
     *  stream, or not prior to each experiment.  Resetting
     *  allows each experiment to use the same underlying random numbers
     *  i.e. common random numbers, this is the default
     * 
     *  Setting it to true indicates that it does reset
     */
    protected boolean myResetStartStreamOption;

    /** indicates whether or not the random variable's
     *  distribution has it stream reset to the next sub-stream
     *  stream, or not, prior to each replication.  Resetting
     *  allows each replication to better ensure that each
     *  replication will be start at the same place in the
     *  sub-streams, thereby, improving synchronization when using
     *  common random numbers.
     *
     *  Setting it to true indicates that it does jump to
     *  the next sub-stream, true is the default
     */
    protected boolean myAdvanceToNextSubStreamOption;

    /** RandomIfc provides a reference to the underlying source of randomness
     *  during the replication
     */
    protected RandomIfc myRandomSource;

    /** RandomIfc provides a reference to the underlying source of randomness
     *  to initialize each replication.
     */
    protected RandomIfc myInitialRandomSource;

    /**
     *  A flag to indicate whether or not a ResponseVariable should be created
     *  and used to capture the randomly generated values. Useful for control variate implementation
     */
    protected boolean myCaptureResponseFlag = false;

    /**
     *  Used to capture the randomly generated values if the capture response flag is true
     */
    protected ResponseVariable myResponse;

    /** Constructs a RandomVariable given the supplied reference to the underlying source of randomness
     * Throws a NullPointerException if the supplied randomness is null
     *
     * @param parent The parent ModelElement
     * @param initialSource The reference to the underlying source of randomness
     */
    public RandomVariable(ModelElement parent, RandomIfc initialSource) {
        this(parent, initialSource, null);
    }

    /** Constructs a RandomVariable given the supplied reference to the underlying source of randomness
     * Throws a NullPointerException if the supplied randomness is null
     *
     * @param parent The parent ModelElement
     * @param initialSource The reference to the underlying source of randomness
     * @param name A string to label the RandomVariable
     */
    public RandomVariable(ModelElement parent, RandomIfc initialSource, String name) {
        super(parent, name);
        setInitialRandomSource(initialSource);
        myRandomSource = myInitialRandomSource;
        setWarmUpOption(false); // do not need to respond to warmup events
        setResetStartStreamOption(true);
        setResetNextSubStreamOption(true);
    }

    @Override
    public final boolean getResetStartStreamOption() {
        return myResetStartStreamOption;
    }

    @Override
    public final void setResetStartStreamOption(boolean b) {
        myResetStartStreamOption = b;
    }

    @Override
    public final boolean getResetNextSubStreamOption() {
        return myAdvanceToNextSubStreamOption;
    }

    @Override
    public final void setResetNextSubStreamOption(boolean b) {
        myAdvanceToNextSubStreamOption = b;
    }

    @Override
    public final void setAntitheticOption(boolean flag) {
        myRandomSource.setAntitheticOption(flag);
    }

    @Override
    public boolean getAntitheticOption() {
        return myRandomSource.getAntitheticOption();
    }

    @Override
    public final void advanceToNextSubstream() {
        myRandomSource.advanceToNextSubstream();
    }

    @Override
    public final void resetStartStream() {
        myRandomSource.resetStartStream();
    }

    @Override
    public final void resetStartSubstream() {
        myRandomSource.resetStartSubstream();
    }

    /** Gets the underlying RVariableIfc for the RandomVariable. This is the
     *  source to which each replication will be initialized
     * @return a RVariableIfc
     */
    public final RandomIfc getInitialRandomSource() {
        return (myInitialRandomSource);
    }

    /** Sets the underlying RandomIfc source for the RandomVariable. This is the
     *  source to which each replication will be initialized.  This is only used
     *  when the replication is initialized. Changing the reference has no effect 
     *  during a replication, since the random variable will continue to use
     *  the reference returned by getRandomSource().  Please also see the 
     *  discussion in the class documentation.
     *  
     *  WARNING: If this is used during an experiment to change the characteristics of
     *  the random source, then each replication may not necessarily start in the
     *  same initial state.  It is recommended that this be used only prior to executing experiments.
     *
     * @param source the reference to the random source, must not be null
     */
    public final void setInitialRandomSource(RandomIfc source) {
        if (source == null) {
            throw new NullPointerException("RandomIfc source must be non-null");
        }
        if (getSimulation().isRunning()){
            JSL.LOGGER.warn("Changes the initial source of {} during a replication.", getName());
        }
        myInitialRandomSource = source;
    }

    /**
     *   Turns on the capturing of the generated random values with a ResponseVariable
     */
    public final void turnOnResponseCapture(){
        if (myCaptureResponseFlag == false){
            myCaptureResponseFlag = true;
            myResponse = new ResponseVariable(this, getName()+":Response");
        }
    }

    /**
     *
     * @return true means that the values are being captured via a ResponseVariable
     */
    public final boolean getCaptureResponseFlag(){
        return myCaptureResponseFlag;
    }

    /** Gets the underlying RandomIfc for the RandomVariable currently
     *  being used during the replication
     *  
     * @return a RandomIfc
     */
    public final RandomIfc getRandomSource() {
        return (myRandomSource);
    }

    /** Sets the underlying RandomIfc source for the RandomVariable.  This
     *  changes the source for the current replication only. The random
     *  variable will start to use this source immediately; however if
     *  a replication is started after this method is called, the random source
     *  will be reassigned to the initial random source before the next replication
     *  is executed.
     *  
     *  To set the random source for the entire experiment (all replications)
     *  use the setInitialRandomSource() method
     *
     * @param source the reference to the random source, must not be null
     */
    public final void setRandomSource(RandomIfc source) {
        if (source == null) {
            throw new NullPointerException("RandomIfc source must be non-null");
        }
        myRandomSource = source;
    }

    /** Each call to getValue() returns a new observation
     * 
     */
    @Override
    public double getValue() {
        double value = myRandomSource.getValue();
        if (getCaptureResponseFlag()){
            myResponse.setValue(value);
        }
        return (value);
    }

    /** Returns the sum of n random draws of the random variable
     *  if n &lt;= 0, then the sum is 0.0
     * 
     * @param n the number to sum
     * @return the sum
     */
    public double getSumOfValues(int n) {
        double sum = 0.0;
        for (int i = 1; i <= n; i++) {
            sum = sum + getValue();
        }
        return sum;
    }

    public final double[] sample(int sampleSize) {
        double[] x = new double[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            x[i] = getValue();
        }
        return (x);
    }

    public final void sample(double[] values) {
        if (values == null) {
            throw new IllegalArgumentException("The supplied array was null");
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = getValue();
        }
    }

    @Override
    public String asString(){
        StringBuilder sb = new StringBuilder();
        sb.append(toString());
        sb.append(myRandomSource.toString());
        return sb.toString();
    }
    @Override
    protected void removedFromModel() {
        super.removedFromModel();
        myRandomSource = null;
        myInitialRandomSource = null;
    }

    /** before any replications reset the underlying random number generator to the
     *  starting stream
     *
     */
    @Override
    protected void beforeExperiment() {
        super.beforeExperiment();
        myRandomSource = myInitialRandomSource;
        if (getResetStartStreamOption()) {
            resetStartStream();
        }
    }

    /** after each replication reset the underlying random number generator to the next
     *  sub-stream
     */
    @Override
    protected void afterReplication() {
        super.afterReplication();

        if (myRandomSource != myInitialRandomSource) {
            // the random source or the initial random source references
            // were changed during the replication
            // make sure that the random source is the same
            // as the initial random source for the next replication
            myRandomSource = myInitialRandomSource;
        }

        if (getResetNextSubStreamOption()) {
            advanceToNextSubstream();
        }

    }
}
