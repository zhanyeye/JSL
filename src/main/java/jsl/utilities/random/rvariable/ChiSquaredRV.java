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
import jsl.utilities.random.rng.RngIfc;

/**
 *  Chi-Squared(degrees of freedom) random variable
 */
public final class ChiSquaredRV extends AbstractRVariable {

    private final double dof;

    public ChiSquaredRV(double dof){
        this(dof, RNStreamFactory.getDefault().getStream());
    }

    public ChiSquaredRV(double dof, RngIfc rng){
        super(rng);
        if (dof <= 0.0) {
            throw new IllegalArgumentException("Chi-Squared degrees of freedom must be > 0.0");
        }
        this.dof = dof;
    }

    /**
     *
     * @param rng the RngIfc to use
     * @return a new instance with same parameter value
     */
    public final ChiSquaredRV newInstance(RngIfc rng){
        return new ChiSquaredRV(this.dof, rng);
    }

    @Override
    public String toString() {
        return "ChiSquaredlRV{" +
                "dof=" + dof +
                '}';
    }

    /**
     *
     * @return the dof value
     */
    public final double getDegreesOfFreedom() {
        return dof;
    }

    @Override
    protected final double generate() {
        double v = JSLRandom.rChiSquared(dof, myRNG);
        return v;
    }
}
