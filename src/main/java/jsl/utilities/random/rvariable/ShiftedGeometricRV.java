package jsl.utilities.random.rvariable;

import jsl.utilities.random.rng.RNStreamFactory;
import jsl.utilities.random.rng.RngIfc;

/**
 *  Shifted Geometric(probability of success) random variable, range 1, 2, 3, ..
 */
public final class ShiftedGeometricRV extends AbstractRVariable {

    private final double myProbSuccess;

    public ShiftedGeometricRV(double prob){
        this(prob, RNStreamFactory.getDefault().getStream());
    }

    public ShiftedGeometricRV(double prob, RngIfc rng){
        super(rng);
        if ((prob < 0.0) || (prob > 1.0)) {
            throw new IllegalArgumentException("Probability must be [0,1]");
        }
        myProbSuccess = prob;
    }

    /**
     *
     * @param rng the RngIfc to use
     * @return a new instance with same parameter value
     */
    public final ShiftedGeometricRV newInstance(RngIfc rng){
        return new ShiftedGeometricRV(this.myProbSuccess, rng);
    }

    @Override
    public String toString() {
        return "ShiftedGeometricRV{" +
                "probSuccess=" + myProbSuccess +
                '}';
    }

    /** Gets the success probability
     * @return The success probability
     */
    public final double getProbabilityOfSuccess() {
        return (myProbSuccess);
    }

    @Override
    protected final double generate() {
        double v = 1.0 + JSLRandom.rGeometric(myProbSuccess, myRNG);
        return v;
    }
}
