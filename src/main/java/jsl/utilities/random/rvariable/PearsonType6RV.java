package jsl.utilities.random.rvariable;

import jsl.utilities.random.rng.RNStreamFactory;
import jsl.utilities.random.rng.RngIfc;

/**
 *  Pearson Type 6(alpha1, alpha2, beta) random variable
 */
public final class PearsonType6RV extends AbstractRVariable {

    private final double myAlpha1;
    private final double myAlpha2;
    private final double myBeta;

    public PearsonType6RV(double alpha1, double alpha2, double beta){
        this(alpha1, alpha2, beta, RNStreamFactory.getDefault().getStream());
    }

    public PearsonType6RV(double alpha1, double alpha2, double beta, RngIfc rng){
        super(rng);
        if (alpha1 <= 0.0) {
            throw new IllegalArgumentException("The 1st shape parameter must be > 0.0");
        }
        if (alpha2 <= 0.0) {
            throw new IllegalArgumentException("The 2nd shape parameter must be > 0.0");
        }
        if (beta <= 0.0) {
            throw new IllegalArgumentException("The scale parameter must be > 0.0");
        }
        myAlpha1 = alpha1;
        myAlpha2 = alpha2;
        myBeta = beta;
    }

    /**
     *
     * @param rng the RngIfc to use
     * @return a new instance with same parameter value
     */
    public final PearsonType6RV newInstance(RngIfc rng){
        return new PearsonType6RV(getAlpha1(), getAlpha2(), myBeta, rng);
    }

    @Override
    public String toString() {
        return "PearsonType6RV{" +
                "alpha1=" + myAlpha1 +
                ", alpha2=" + myAlpha2 +
                ", beta=" + myBeta +
                '}';
    }

    public final double getAlpha1() {
        return myAlpha1;
    }

    public final double getAlpha2() {
        return myAlpha2;
    }

    public final double getBeta() {
        return myBeta;
    }

    @Override
    protected final double generate() {
        double v = JSLRandom.rPearsonType6(myAlpha1, myAlpha2, myBeta, myRNG);
        return v;
    }
}
