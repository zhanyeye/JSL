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

package jsl.utilities.random.rvariable;

import jsl.utilities.random.rng.RNStreamFactory;
import jsl.utilities.random.rng.RNStreamIfc;

/**
 *  Exponential(mean) random variable
 */
public final class ExponentialRV extends AbstractRVariable {

    private final double mean;

    public ExponentialRV(double mean){
        this(mean, RNStreamFactory.getDefaultFactory().getStream());
    }

    public ExponentialRV(double mean, RNStreamIfc rng){
        super(rng);
        if (mean <= 0.0) {
            throw new IllegalArgumentException("Exponential mean must be > 0.0");
        }
        this.mean = mean;
    }

    /**
     *
     * @param rng the RngIfc to use
     * @return a new instance with same parameter value
     */
    public final ExponentialRV newInstance(RNStreamIfc rng){
        return new ExponentialRV(this.mean, rng);
    }

    @Override
    public String toString() {
        return "ExponentialRV{" +
                "mean=" + mean +
                '}';
    }

    /**
     *
     * @return the mean value
     */
    public final double getMean() {
        return mean;
    }

    @Override
    protected final double generate() {
        double v = JSLRandom.rExponential(mean, myRNG);
        return v;
    }

    /**
     * The key is "mean" with default value 1.0
     *
     * @return a control for Exponential random variables
     */
    public static RVControls makeControls() {
        return new RVControls(RVariableIfc.RVType.Exponential) {
            @Override
            protected final void fillControls() {
                addDoubleControl("mean", 1.0);
                setName(RVariableIfc.RVType.Exponential.name());
            }

            public final RVariableIfc makeRVariabel(RNStreamIfc rnStream) {
                double mean = getDoubleControl("mean");
                return new ExponentialRV(mean, rnStream);
            }
        };
    }
}
