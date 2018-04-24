/**
 * 
 */
package jsl.modeling.elements.variable;

import jsl.modeling.ModelElement;

/**
 * @author rossetti
 *
 */
public class AveragePerTimeWeightedVariable extends AggregateTimeWeightedVariable {

    /**
     * @param parent
     */
    public AveragePerTimeWeightedVariable(ModelElement parent) {
        this(parent, null);
    }

    /**
     * @param parent
     * @param name
     */
    public AveragePerTimeWeightedVariable(ModelElement parent, String name) {
        super(parent, name);

    }

    /* (non-Javadoc)
     * @see jsl.modeling.elements.variable2.Aggregate#valueChanged(jsl.modeling.elements.variable2.Aggregatable)
     */
    @Override
    protected void valueChangedBeforeReplication(Aggregatable variable) {
        // a variable has just been changed before the replication
        // still need to make the variable have the correct value
        myAggTW.assignInitialValue(avgValues());
        notifyAggregatesOfValueChange();
    }

    @Override
    protected void valueChangedDuringReplication(Aggregatable variable) {
        double n = myVariables.size();
        double sum = getValue(); // current value
        sum = sum - (variable.getPreviousValue() / n); // subtract off the old value
        sum = sum + (variable.getValue() / n); // add on the new value
        myAggTW.setValue(sum); //record the new value			
        notifyAggregatesOfValueChange();
    }

    /* (non-Javadoc)
     * @see jsl.modeling.elements.variable2.Aggregate#variableAdded(jsl.modeling.elements.variable2.Aggregatable)
     */
    @Override
    protected void variableAddedBeforeReplication(Aggregatable variable) {
        // a new variable has just been attached to the aggregate
        // either before or after the start of a replication
        // still need to make the variable have the correct value
        myAggTW.assignInitialValue(avgValues());
        notifyAggregatesOfValueChange();
    }

    @Override
    protected void variableAddedDuringReplication(Aggregatable variable) {
        // a new variable has just been attached to the aggregate
        // either before or after the start of a replication
        // still need to make the variable have the correct value
        myAggTW.setValue(avgValues());
        notifyAggregatesOfValueChange();
    }

    /* (non-Javadoc)
     * @see jsl.modeling.elements.variable2.Aggregate#variableRemoved(jsl.modeling.elements.variable2.Aggregatable)
     */
    @Override
    protected void variableRemovedBeforeReplication(Aggregatable variable) {
        // a new variable has just been removed from the aggregate
        // either before or after the start of a replication
        // still need to make the variable have the correct value
        myAggTW.assignInitialValue(avgValues());
        notifyAggregatesOfValueChange();
    }

    @Override
    protected void variableRemovedDuringReplication(Aggregatable variable) {
        // a new variable has just been removed from the aggregate
        // either before or after the start of a replication
        // still need to make the variable have the correct value
        myAggTW.setValue(avgValues());
        notifyAggregatesOfValueChange();
    }
}
