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

import jsl.utilities.jsldbsrc.tables.ModelElement;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class ModelElementRecord extends UpdatableRecordImpl<ModelElementRecord> implements Record5<Integer, String, Long, String, String> {

    private static final long serialVersionUID = 1281144150;

    /**
     * Setter for <code>APP.MODEL_ELEMENT.SIM_RUN_ID_FK</code>.
     */
    public void setSimRunIdFk(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>APP.MODEL_ELEMENT.SIM_RUN_ID_FK</code>.
     */
    public Integer getSimRunIdFk() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>APP.MODEL_ELEMENT.ELEMENT_NAME</code>.
     */
    public void setElementName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>APP.MODEL_ELEMENT.ELEMENT_NAME</code>.
     */
    public String getElementName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>APP.MODEL_ELEMENT.ELEMENT_ID</code>.
     */
    public void setElementId(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>APP.MODEL_ELEMENT.ELEMENT_ID</code>.
     */
    public Long getElementId() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>APP.MODEL_ELEMENT.CLASS_NAME</code>.
     */
    public void setClassName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>APP.MODEL_ELEMENT.CLASS_NAME</code>.
     */
    public String getClassName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>APP.MODEL_ELEMENT.PARENT_NAME_FK</code>.
     */
    public void setParentNameFk(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>APP.MODEL_ELEMENT.PARENT_NAME_FK</code>.
     */
    public String getParentNameFk() {
        return (String) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record2<Integer, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Integer, String, Long, String, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<Integer, String, Long, String, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return ModelElement.MODEL_ELEMENT.SIM_RUN_ID_FK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return ModelElement.MODEL_ELEMENT.ELEMENT_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return ModelElement.MODEL_ELEMENT.ELEMENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return ModelElement.MODEL_ELEMENT.CLASS_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return ModelElement.MODEL_ELEMENT.PARENT_NAME_FK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getSimRunIdFk();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getElementName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getElementId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getClassName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getParentNameFk();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getSimRunIdFk();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getElementName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getElementId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getClassName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getParentNameFk();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElementRecord value1(Integer value) {
        setSimRunIdFk(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElementRecord value2(String value) {
        setElementName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElementRecord value3(Long value) {
        setElementId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElementRecord value4(String value) {
        setClassName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElementRecord value5(String value) {
        setParentNameFk(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelElementRecord values(Integer value1, String value2, Long value3, String value4, String value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ModelElementRecord
     */
    public ModelElementRecord() {
        super(ModelElement.MODEL_ELEMENT);
    }

    /**
     * Create a detached, initialised ModelElementRecord
     */
    public ModelElementRecord(Integer simRunIdFk, String elementName, Long elementId, String className, String parentNameFk) {
        super(ModelElement.MODEL_ELEMENT);

        set(0, simRunIdFk);
        set(1, elementName);
        set(2, elementId);
        set(3, className);
        set(4, parentNameFk);
    }
}
