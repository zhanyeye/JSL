package jsl.utilities.random.rvariable;

import jsl.utilities.random.rng.RNStreamFactory;
import jsl.utilities.random.rng.RngIfc;

/**
 *  Pearson Type 5(shape, scale) random variable
 */
public final class PearsonType5RV extends AbstractRVariable {

    private final double myShape;
    private final double myScale;

    public PearsonType5RV(double shape, double scale){
        this(shape, scale, RNStreamFactory.getDefault().getStream());
    }

    public PearsonType5RV(double shape, double scale, RngIfc rng){
        super(rng);
        if (shape <= 0) {
            throw new IllegalArgumentException("Shape parameter must be positive");
        }
        if (scale <= 0) {
            throw new IllegalArgumentException("Scale parameter must be positive");
        }
        this.myShape = shape;
        this.myScale = scale;
    }

    /**
     *
     * @param rng the RngIfc to use
     * @return a new instance with same parameter value
     */
    public final PearsonType5RV newInstance(RngIfc rng){
        return new PearsonType5RV(this.getShape(), this.getScale(), rng);
    }

    @Override
    public String toString() {
        return "PearsonType5RV{" +
                "shape=" + myShape +
                ", scale=" + myScale +
                '}';
    }

    /** Gets the shape
     * @return The shape parameter as a double
     */
    public double getShape() {
        return myShape;
    }

    /** Gets the scale parameter
     * @return The scale parameter as a double
     */
    public double getScale() {
        return myScale;
    }

    @Override
    protected final double generate() {
        double v = JSLRandom.rPearsonType5(myShape, myScale, myRNG);
        return v;
    }
}
