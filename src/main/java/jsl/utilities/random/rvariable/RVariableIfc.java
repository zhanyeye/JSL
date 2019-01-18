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

package jsl.utilities.random.rvariable;

import jsl.utilities.PreviousValueIfc;
import jsl.utilities.random.RandomIfc;
import jsl.utilities.random.SampleIfc;
import jsl.utilities.random.rng.GetRandomNumberStreamIfc;
import jsl.utilities.random.rng.RNStreamIfc;
import jsl.utilities.random.rng.SetRandomNumberStreamIfc;

import java.util.function.DoubleSupplier;
import java.util.stream.DoubleStream;

/**
 * An interface for defining random variables. The methods sample() and getValue() gets a new
 * value of the random variable sampled accordingly.  The method getPreviousValue() returns
 * the value from the last call to sample()/getValue(). The value returned by getPreviousValue() stays
 * the same until the next call to sample()/getValue().  The methods sample()/getValue() always get
 * the next random value.  If sample()/getValue() is never called then getPreviousValue() returns Double.NaN.
 * Use sample()/getValue() to get a new random value and use getPreviousValue() to get the last sampled value.
 *
 * The preferred approach to creating random variables is to sub-class AbstractRVariable.
 */
public interface RVariableIfc extends RandomIfc, SampleIfc, NewAntitheticInstanceIfc,
        PreviousValueIfc, DoubleSupplier, SetRandomNumberStreamIfc, GetRandomNumberStreamIfc {

    /**
     * The set of pre-defined distribution types
     */
    enum RVType {
        Bernoulli, Beta, ChiSquared, Binomial, Constant, DUniform, Exponential,
        Gamma, GeneralizedBeta, Geometric, JohnsonB, Laplace, LogLogistic, Lognormal,
        NegativeBinomial, Normal, PearsonType5, PearsonType6, Poisson, ShiftedGeometric,
        Triangular, Uniform, Weibull, DEmpirical
    }

    /**
     * @return returns a sampled values
     */
    default double getValue() {
        return sample();
    }

    /**
     * @param rng the RNStreamIfc to use
     * @return a new instance with same parameter values
     */
    RVariableIfc newInstance(RNStreamIfc rng);

    /**
     * @return a new instance with same parameter values, with a different stream
     */
    default RVariableIfc newInstance() {
        return newInstance(JSLRandom.createRNStream());
    }

    /** This method facilitates turning instances of RVariableIfc into Java DoubleStream
     * for use in the Stream API
     *
     * @return the generated random number using sample()
     */
    @Override
    default double getAsDouble() {
        return sample();
    }

    /** Turns the doubles into a DoubleStream for the Stream API
     *
     * @return  a DoubleStream representation of the random variable
     */
    default DoubleStream asDoubleStream(){
        return DoubleStream.generate(this);
    }

}
