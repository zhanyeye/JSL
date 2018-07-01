/*
 * This file is generated by jOOQ.
*/
package jsl.utilities.jsldbsrc.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import jsl.utilities.jsldbsrc.JslDb;
import jsl.utilities.jsldbsrc.Keys;
import jsl.utilities.jsldbsrc.tables.records.ModelElementRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class ModelElement extends TableImpl<ModelElementRecord> {

    private static final long serialVersionUID = -1968773197;

    /**
     * The reference instance of <code>JSL_DB.MODEL_ELEMENT</code>
     */
    public static final ModelElement MODEL_ELEMENT = new ModelElement();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ModelElementRecord> getRecordType() {
        return ModelElementRecord.class;
    }

    /**
     * The column <code>JSL_DB.MODEL_ELEMENT.SIM_RUN_ID_FK</code>.
     */
    public final TableField<ModelElementRecord, Integer> SIM_RUN_ID_FK = createField("SIM_RUN_ID_FK", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>JSL_DB.MODEL_ELEMENT.ELEMENT_NAME</code>.
     */
    public final TableField<ModelElementRecord, String> ELEMENT_NAME = createField("ELEMENT_NAME", org.jooq.impl.SQLDataType.VARCHAR(510).nullable(false), this, "");

    /**
     * The column <code>JSL_DB.MODEL_ELEMENT.ELEMENT_ID</code>.
     */
    public final TableField<ModelElementRecord, Long> ELEMENT_ID = createField("ELEMENT_ID", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>JSL_DB.MODEL_ELEMENT.CLASS_NAME</code>.
     */
    public final TableField<ModelElementRecord, String> CLASS_NAME = createField("CLASS_NAME", org.jooq.impl.SQLDataType.VARCHAR(510).nullable(false), this, "");

    /**
     * The column <code>JSL_DB.MODEL_ELEMENT.PARENT_NAME_FK</code>.
     */
    public final TableField<ModelElementRecord, String> PARENT_NAME_FK = createField("PARENT_NAME_FK", org.jooq.impl.SQLDataType.VARCHAR(510), this, "");

    /**
     * Create a <code>JSL_DB.MODEL_ELEMENT</code> table reference
     */
    public ModelElement() {
        this(DSL.name("MODEL_ELEMENT"), null);
    }

    /**
     * Create an aliased <code>JSL_DB.MODEL_ELEMENT</code> table reference
     */
    public ModelElement(String alias) {
        this(DSL.name(alias), MODEL_ELEMENT);
    }

    /**
     * Create an aliased <code>JSL_DB.MODEL_ELEMENT</code> table reference
     */
    public ModelElement(Name alias) {
        this(alias, MODEL_ELEMENT);
    }

    private ModelElement(Name alias, Table<ModelElementRecord> aliased) {
        this(alias, aliased, null);
    }

    private ModelElement(Name alias, Table<ModelElementRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return JslDb.JSL_DB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ModelElementRecord> getPrimaryKey() {
        return Keys.ME_PRIM_KY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ModelElementRecord>> getKeys() {
        return Arrays.<UniqueKey<ModelElementRecord>>asList(Keys.ME_PRIM_KY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ModelElementRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ModelElementRecord, ?>>asList(Keys.ME_SIMRUN_FK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElement as(String alias) {
        return new ModelElement(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElement as(Name alias) {
        return new ModelElement(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public ModelElement rename(String name) {
        return new ModelElement(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ModelElement rename(Name name) {
        return new ModelElement(name, null);
    }
}
