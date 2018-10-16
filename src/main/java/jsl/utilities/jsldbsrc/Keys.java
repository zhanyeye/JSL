/*
 * This file is generated by jOOQ.
*/
package jsl.utilities.jsldbsrc;


import javax.annotation.Generated;

import jsl.utilities.jsldbsrc.tables.AcrossRepStat;
import jsl.utilities.jsldbsrc.tables.BatchStat;
import jsl.utilities.jsldbsrc.tables.ModelElement;
import jsl.utilities.jsldbsrc.tables.SimulationRun;
import jsl.utilities.jsldbsrc.tables.WithinRepCounterStat;
import jsl.utilities.jsldbsrc.tables.WithinRepStat;
import jsl.utilities.jsldbsrc.tables.records.AcrossRepStatRecord;
import jsl.utilities.jsldbsrc.tables.records.BatchStatRecord;
import jsl.utilities.jsldbsrc.tables.records.ModelElementRecord;
import jsl.utilities.jsldbsrc.tables.records.SimulationRunRecord;
import jsl.utilities.jsldbsrc.tables.records.WithinRepCounterStatRecord;
import jsl.utilities.jsldbsrc.tables.records.WithinRepStatRecord;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>JSL_DB</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<AcrossRepStatRecord, Integer> IDENTITY_ACROSS_REP_STAT = Identities0.IDENTITY_ACROSS_REP_STAT;
    public static final Identity<BatchStatRecord, Integer> IDENTITY_BATCH_STAT = Identities0.IDENTITY_BATCH_STAT;
    public static final Identity<SimulationRunRecord, Integer> IDENTITY_SIMULATION_RUN = Identities0.IDENTITY_SIMULATION_RUN;
    public static final Identity<WithinRepCounterStatRecord, Integer> IDENTITY_WITHIN_REP_COUNTER_STAT = Identities0.IDENTITY_WITHIN_REP_COUNTER_STAT;
    public static final Identity<WithinRepStatRecord, Integer> IDENTITY_WITHIN_REP_STAT = Identities0.IDENTITY_WITHIN_REP_STAT;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AcrossRepStatRecord> SQL181016084244620 = UniqueKeys0.SQL181016084244620;
    public static final UniqueKey<BatchStatRecord> SQL181016084244730 = UniqueKeys0.SQL181016084244730;
    public static final UniqueKey<ModelElementRecord> ME_PRIM_KY = UniqueKeys0.ME_PRIM_KY;
    public static final UniqueKey<ModelElementRecord> ME_NAME_UNIQUE = UniqueKeys0.ME_NAME_UNIQUE;
    public static final UniqueKey<SimulationRunRecord> SQL181016084244380 = UniqueKeys0.SQL181016084244380;
    public static final UniqueKey<SimulationRunRecord> SR_NAME_EXP_UNIQUE = UniqueKeys0.SR_NAME_EXP_UNIQUE;
    public static final UniqueKey<WithinRepCounterStatRecord> SQL181016084244680 = UniqueKeys0.SQL181016084244680;
    public static final UniqueKey<WithinRepCounterStatRecord> WRCS_UNIQUE_ELEMENT_SIMRUN_REPNUM = UniqueKeys0.WRCS_UNIQUE_ELEMENT_SIMRUN_REPNUM;
    public static final UniqueKey<WithinRepStatRecord> SQL181016084244550 = UniqueKeys0.SQL181016084244550;
    public static final UniqueKey<WithinRepStatRecord> WRS_UNIQUE_ELEMENT_SIMRUN_REPNUM = UniqueKeys0.WRS_UNIQUE_ELEMENT_SIMRUN_REPNUM;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<AcrossRepStatRecord, ModelElementRecord> ARS_MODEL_ELEMENT_FK = ForeignKeys0.ARS_MODEL_ELEMENT_FK;
    public static final ForeignKey<AcrossRepStatRecord, SimulationRunRecord> ARS_SIMRUN_FK = ForeignKeys0.ARS_SIMRUN_FK;
    public static final ForeignKey<BatchStatRecord, ModelElementRecord> BS_MODEL_ELEMENT_FK = ForeignKeys0.BS_MODEL_ELEMENT_FK;
    public static final ForeignKey<BatchStatRecord, SimulationRunRecord> BS_SIMRUN_FK = ForeignKeys0.BS_SIMRUN_FK;
    public static final ForeignKey<ModelElementRecord, SimulationRunRecord> ME_SIMRUN_FK = ForeignKeys0.ME_SIMRUN_FK;
    public static final ForeignKey<WithinRepCounterStatRecord, ModelElementRecord> WRCS_MODEL_ELEMENT_FK = ForeignKeys0.WRCS_MODEL_ELEMENT_FK;
    public static final ForeignKey<WithinRepCounterStatRecord, SimulationRunRecord> WRCS_SIMRUN_FK = ForeignKeys0.WRCS_SIMRUN_FK;
    public static final ForeignKey<WithinRepStatRecord, ModelElementRecord> WRS_MODEL_ELEMENT_FK = ForeignKeys0.WRS_MODEL_ELEMENT_FK;
    public static final ForeignKey<WithinRepStatRecord, SimulationRunRecord> WRS_SIMRUN_FK = ForeignKeys0.WRS_SIMRUN_FK;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 extends AbstractKeys {
        public static Identity<AcrossRepStatRecord, Integer> IDENTITY_ACROSS_REP_STAT = createIdentity(AcrossRepStat.ACROSS_REP_STAT, AcrossRepStat.ACROSS_REP_STAT.ID);
        public static Identity<BatchStatRecord, Integer> IDENTITY_BATCH_STAT = createIdentity(BatchStat.BATCH_STAT, BatchStat.BATCH_STAT.ID);
        public static Identity<SimulationRunRecord, Integer> IDENTITY_SIMULATION_RUN = createIdentity(SimulationRun.SIMULATION_RUN, SimulationRun.SIMULATION_RUN.ID);
        public static Identity<WithinRepCounterStatRecord, Integer> IDENTITY_WITHIN_REP_COUNTER_STAT = createIdentity(WithinRepCounterStat.WITHIN_REP_COUNTER_STAT, WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.ID);
        public static Identity<WithinRepStatRecord, Integer> IDENTITY_WITHIN_REP_STAT = createIdentity(WithinRepStat.WITHIN_REP_STAT, WithinRepStat.WITHIN_REP_STAT.ID);
    }

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<AcrossRepStatRecord> SQL181016084244620 = createUniqueKey(AcrossRepStat.ACROSS_REP_STAT, "SQL181016084244620", AcrossRepStat.ACROSS_REP_STAT.ID);
        public static final UniqueKey<BatchStatRecord> SQL181016084244730 = createUniqueKey(BatchStat.BATCH_STAT, "SQL181016084244730", BatchStat.BATCH_STAT.ID);
        public static final UniqueKey<ModelElementRecord> ME_PRIM_KY = createUniqueKey(ModelElement.MODEL_ELEMENT, "ME_PRIM_KY", ModelElement.MODEL_ELEMENT.SIM_RUN_ID_FK, ModelElement.MODEL_ELEMENT.ELEMENT_ID);
        public static final UniqueKey<ModelElementRecord> ME_NAME_UNIQUE = createUniqueKey(ModelElement.MODEL_ELEMENT, "ME_NAME_UNIQUE", ModelElement.MODEL_ELEMENT.SIM_RUN_ID_FK, ModelElement.MODEL_ELEMENT.ELEMENT_NAME);
        public static final UniqueKey<SimulationRunRecord> SQL181016084244380 = createUniqueKey(SimulationRun.SIMULATION_RUN, "SQL181016084244380", SimulationRun.SIMULATION_RUN.ID);
        public static final UniqueKey<SimulationRunRecord> SR_NAME_EXP_UNIQUE = createUniqueKey(SimulationRun.SIMULATION_RUN, "SR_NAME_EXP_UNIQUE", SimulationRun.SIMULATION_RUN.SIM_NAME, SimulationRun.SIMULATION_RUN.EXP_NAME);
        public static final UniqueKey<WithinRepCounterStatRecord> SQL181016084244680 = createUniqueKey(WithinRepCounterStat.WITHIN_REP_COUNTER_STAT, "SQL181016084244680", WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.ID);
        public static final UniqueKey<WithinRepCounterStatRecord> WRCS_UNIQUE_ELEMENT_SIMRUN_REPNUM = createUniqueKey(WithinRepCounterStat.WITHIN_REP_COUNTER_STAT, "WRCS_UNIQUE_ELEMENT_SIMRUN_REPNUM", WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.ELEMENT_ID_FK, WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.SIM_RUN_ID_FK, WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.REP_NUM);
        public static final UniqueKey<WithinRepStatRecord> SQL181016084244550 = createUniqueKey(WithinRepStat.WITHIN_REP_STAT, "SQL181016084244550", WithinRepStat.WITHIN_REP_STAT.ID);
        public static final UniqueKey<WithinRepStatRecord> WRS_UNIQUE_ELEMENT_SIMRUN_REPNUM = createUniqueKey(WithinRepStat.WITHIN_REP_STAT, "WRS_UNIQUE_ELEMENT_SIMRUN_REPNUM", WithinRepStat.WITHIN_REP_STAT.ELEMENT_ID_FK, WithinRepStat.WITHIN_REP_STAT.SIM_RUN_ID_FK, WithinRepStat.WITHIN_REP_STAT.REP_NUM);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<AcrossRepStatRecord, ModelElementRecord> ARS_MODEL_ELEMENT_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.ME_PRIM_KY, AcrossRepStat.ACROSS_REP_STAT, "ARS_MODEL_ELEMENT_FK", AcrossRepStat.ACROSS_REP_STAT.SIM_RUN_ID_FK, AcrossRepStat.ACROSS_REP_STAT.ELEMENT_ID_FK);
        public static final ForeignKey<AcrossRepStatRecord, SimulationRunRecord> ARS_SIMRUN_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.SQL181016084244380, AcrossRepStat.ACROSS_REP_STAT, "ARS_SIMRUN_FK", AcrossRepStat.ACROSS_REP_STAT.SIM_RUN_ID_FK);
        public static final ForeignKey<BatchStatRecord, ModelElementRecord> BS_MODEL_ELEMENT_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.ME_PRIM_KY, BatchStat.BATCH_STAT, "BS_MODEL_ELEMENT_FK", BatchStat.BATCH_STAT.SIM_RUN_ID_FK, BatchStat.BATCH_STAT.ELEMENT_ID_FK);
        public static final ForeignKey<BatchStatRecord, SimulationRunRecord> BS_SIMRUN_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.SQL181016084244380, BatchStat.BATCH_STAT, "BS_SIMRUN_FK", BatchStat.BATCH_STAT.SIM_RUN_ID_FK);
        public static final ForeignKey<ModelElementRecord, SimulationRunRecord> ME_SIMRUN_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.SQL181016084244380, ModelElement.MODEL_ELEMENT, "ME_SIMRUN_FK", ModelElement.MODEL_ELEMENT.SIM_RUN_ID_FK);
        public static final ForeignKey<WithinRepCounterStatRecord, ModelElementRecord> WRCS_MODEL_ELEMENT_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.ME_PRIM_KY, WithinRepCounterStat.WITHIN_REP_COUNTER_STAT, "WRCS_MODEL_ELEMENT_FK", WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.SIM_RUN_ID_FK, WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.ELEMENT_ID_FK);
        public static final ForeignKey<WithinRepCounterStatRecord, SimulationRunRecord> WRCS_SIMRUN_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.SQL181016084244380, WithinRepCounterStat.WITHIN_REP_COUNTER_STAT, "WRCS_SIMRUN_FK", WithinRepCounterStat.WITHIN_REP_COUNTER_STAT.SIM_RUN_ID_FK);
        public static final ForeignKey<WithinRepStatRecord, ModelElementRecord> WRS_MODEL_ELEMENT_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.ME_PRIM_KY, WithinRepStat.WITHIN_REP_STAT, "WRS_MODEL_ELEMENT_FK", WithinRepStat.WITHIN_REP_STAT.SIM_RUN_ID_FK, WithinRepStat.WITHIN_REP_STAT.ELEMENT_ID_FK);
        public static final ForeignKey<WithinRepStatRecord, SimulationRunRecord> WRS_SIMRUN_FK = createForeignKey(jsl.utilities.jsldbsrc.Keys.SQL181016084244380, WithinRepStat.WITHIN_REP_STAT, "WRS_SIMRUN_FK", WithinRepStat.WITHIN_REP_STAT.SIM_RUN_ID_FK);
    }
}
