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
 * This file is generated by jOOQ.
*/
package jsl.utilities.jsldbsrc.tables.records;


import javax.annotation.Generated;

import jsl.utilities.jsldbsrc.tables.BatchStat;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BatchStatRecord extends UpdatableRecordImpl<BatchStatRecord> {

    private static final long serialVersionUID = -475722684;

    /**
     * Setter for <code>APP.BATCH_STAT.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.MODEL_ELEMENT_NAME</code>.
     */
    public void setModelElementName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.MODEL_ELEMENT_NAME</code>.
     */
    public String getModelElementName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.SIM_RUN_ID_FK</code>.
     */
    public void setSimRunIdFk(Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.SIM_RUN_ID_FK</code>.
     */
    public Integer getSimRunIdFk() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.STAT_NAME</code>.
     */
    public void setStatName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.STAT_NAME</code>.
     */
    public String getStatName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.REP_NUM</code>.
     */
    public void setRepNum(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.REP_NUM</code>.
     */
    public Integer getRepNum() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.STAT_COUNT</code>.
     */
    public void setStatCount(Double value) {
        set(5, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.STAT_COUNT</code>.
     */
    public Double getStatCount() {
        return (Double) get(5);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.AVERAGE</code>.
     */
    public void setAverage(Double value) {
        set(6, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.AVERAGE</code>.
     */
    public Double getAverage() {
        return (Double) get(6);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.STD_DEV</code>.
     */
    public void setStdDev(Double value) {
        set(7, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.STD_DEV</code>.
     */
    public Double getStdDev() {
        return (Double) get(7);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.STD_ERR</code>.
     */
    public void setStdErr(Double value) {
        set(8, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.STD_ERR</code>.
     */
    public Double getStdErr() {
        return (Double) get(8);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.HALF_WIDTH</code>.
     */
    public void setHalfWidth(Double value) {
        set(9, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.HALF_WIDTH</code>.
     */
    public Double getHalfWidth() {
        return (Double) get(9);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.CONF_LEVEL</code>.
     */
    public void setConfLevel(Double value) {
        set(10, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.CONF_LEVEL</code>.
     */
    public Double getConfLevel() {
        return (Double) get(10);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.MINIMUM</code>.
     */
    public void setMinimum(Double value) {
        set(11, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.MINIMUM</code>.
     */
    public Double getMinimum() {
        return (Double) get(11);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.MAXIMUM</code>.
     */
    public void setMaximum(Double value) {
        set(12, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.MAXIMUM</code>.
     */
    public Double getMaximum() {
        return (Double) get(12);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.WEIGHTED_SUM</code>.
     */
    public void setWeightedSum(Double value) {
        set(13, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.WEIGHTED_SUM</code>.
     */
    public Double getWeightedSum() {
        return (Double) get(13);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.SUM_OF_WEIGHTS</code>.
     */
    public void setSumOfWeights(Double value) {
        set(14, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.SUM_OF_WEIGHTS</code>.
     */
    public Double getSumOfWeights() {
        return (Double) get(14);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.WEIGHTED_SSQ</code>.
     */
    public void setWeightedSsq(Double value) {
        set(15, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.WEIGHTED_SSQ</code>.
     */
    public Double getWeightedSsq() {
        return (Double) get(15);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.DEV_SSQ</code>.
     */
    public void setDevSsq(Double value) {
        set(16, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.DEV_SSQ</code>.
     */
    public Double getDevSsq() {
        return (Double) get(16);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.LAST_VALUE</code>.
     */
    public void setLastValue(Double value) {
        set(17, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.LAST_VALUE</code>.
     */
    public Double getLastValue() {
        return (Double) get(17);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.LAST_WEIGHT</code>.
     */
    public void setLastWeight(Double value) {
        set(18, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.LAST_WEIGHT</code>.
     */
    public Double getLastWeight() {
        return (Double) get(18);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.KURTOSIS</code>.
     */
    public void setKurtosis(Double value) {
        set(19, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.KURTOSIS</code>.
     */
    public Double getKurtosis() {
        return (Double) get(19);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.SKEWNESS</code>.
     */
    public void setSkewness(Double value) {
        set(20, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.SKEWNESS</code>.
     */
    public Double getSkewness() {
        return (Double) get(20);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.LAG1_COV</code>.
     */
    public void setLag1Cov(Double value) {
        set(21, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.LAG1_COV</code>.
     */
    public Double getLag1Cov() {
        return (Double) get(21);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.LAG1_CORR</code>.
     */
    public void setLag1Corr(Double value) {
        set(22, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.LAG1_CORR</code>.
     */
    public Double getLag1Corr() {
        return (Double) get(22);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.VON_NEUMAN_LAG1_STAT</code>.
     */
    public void setVonNeumanLag1Stat(Double value) {
        set(23, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.VON_NEUMAN_LAG1_STAT</code>.
     */
    public Double getVonNeumanLag1Stat() {
        return (Double) get(23);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.NUM_MISSING_OBS</code>.
     */
    public void setNumMissingObs(Double value) {
        set(24, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.NUM_MISSING_OBS</code>.
     */
    public Double getNumMissingObs() {
        return (Double) get(24);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.MIN_BATCH_SIZE</code>.
     */
    public void setMinBatchSize(Double value) {
        set(25, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.MIN_BATCH_SIZE</code>.
     */
    public Double getMinBatchSize() {
        return (Double) get(25);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.MIN_NUM_BATCHES</code>.
     */
    public void setMinNumBatches(Double value) {
        set(26, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.MIN_NUM_BATCHES</code>.
     */
    public Double getMinNumBatches() {
        return (Double) get(26);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.MAX_NUM_BATCHES_MULTIPLE</code>.
     */
    public void setMaxNumBatchesMultiple(Double value) {
        set(27, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.MAX_NUM_BATCHES_MULTIPLE</code>.
     */
    public Double getMaxNumBatchesMultiple() {
        return (Double) get(27);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.MAX_NUM_BATCHES</code>.
     */
    public void setMaxNumBatches(Double value) {
        set(28, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.MAX_NUM_BATCHES</code>.
     */
    public Double getMaxNumBatches() {
        return (Double) get(28);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.NUM_REBATCHES</code>.
     */
    public void setNumRebatches(Double value) {
        set(29, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.NUM_REBATCHES</code>.
     */
    public Double getNumRebatches() {
        return (Double) get(29);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.CURRENT_BATCH_SIZE</code>.
     */
    public void setCurrentBatchSize(Double value) {
        set(30, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.CURRENT_BATCH_SIZE</code>.
     */
    public Double getCurrentBatchSize() {
        return (Double) get(30);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.AMT_UNBATCHED</code>.
     */
    public void setAmtUnbatched(Double value) {
        set(31, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.AMT_UNBATCHED</code>.
     */
    public Double getAmtUnbatched() {
        return (Double) get(31);
    }

    /**
     * Setter for <code>APP.BATCH_STAT.TOTAL_NUM_OBS</code>.
     */
    public void setTotalNumObs(Double value) {
        set(32, value);
    }

    /**
     * Getter for <code>APP.BATCH_STAT.TOTAL_NUM_OBS</code>.
     */
    public Double getTotalNumObs() {
        return (Double) get(32);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BatchStatRecord
     */
    public BatchStatRecord() {
        super(BatchStat.BATCH_STAT);
    }

    /**
     * Create a detached, initialised BatchStatRecord
     */
    public BatchStatRecord(Integer id, String modelElementName, Integer simRunIdFk, String statName, Integer repNum, Double statCount, Double average, Double stdDev, Double stdErr, Double halfWidth, Double confLevel, Double minimum, Double maximum, Double weightedSum, Double sumOfWeights, Double weightedSsq, Double devSsq, Double lastValue, Double lastWeight, Double kurtosis, Double skewness, Double lag1Cov, Double lag1Corr, Double vonNeumanLag1Stat, Double numMissingObs, Double minBatchSize, Double minNumBatches, Double maxNumBatchesMultiple, Double maxNumBatches, Double numRebatches, Double currentBatchSize, Double amtUnbatched, Double totalNumObs) {
        super(BatchStat.BATCH_STAT);

        set(0, id);
        set(1, modelElementName);
        set(2, simRunIdFk);
        set(3, statName);
        set(4, repNum);
        set(5, statCount);
        set(6, average);
        set(7, stdDev);
        set(8, stdErr);
        set(9, halfWidth);
        set(10, confLevel);
        set(11, minimum);
        set(12, maximum);
        set(13, weightedSum);
        set(14, sumOfWeights);
        set(15, weightedSsq);
        set(16, devSsq);
        set(17, lastValue);
        set(18, lastWeight);
        set(19, kurtosis);
        set(20, skewness);
        set(21, lag1Cov);
        set(22, lag1Corr);
        set(23, vonNeumanLag1Stat);
        set(24, numMissingObs);
        set(25, minBatchSize);
        set(26, minNumBatches);
        set(27, maxNumBatchesMultiple);
        set(28, maxNumBatches);
        set(29, numRebatches);
        set(30, currentBatchSize);
        set(31, amtUnbatched);
        set(32, totalNumObs);
    }
}
