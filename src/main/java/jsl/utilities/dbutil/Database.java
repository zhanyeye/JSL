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

package jsl.utilities.dbutil;

import jsl.utilities.excel.ExcelUtil;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 *  A concrete implementation of the DatabaseIfc interface.
 *
 * Many databases define the terms database, user, schema in a variety of ways. This abstraction
 * defines this concept as the userSchema.  It is the name of the organizational construct for
 * which the user defined database object are contained. These are not the system abstractions.
 * The database name provided to the construct is for labeling and may or may not have any relationship
 * to the actual file name or database name of the database. The supplied connection has all
 * the information that it needs to access the database.
 */
public class Database implements DatabaseIfc {

    static {
        System.setProperty("org.jooq.no-logo", "true");
    }

    private final String myName;
    private final Schema myUserSchema;
    private final Connection myConnection;
    private final SQLDialect mySQLDialect;
    private Settings myExecuteLoggingSettings;

    /**
     * @param dbName         a string representing the name of the database must not be null. This name
     *                       may or may not have any relation to the actual name of the database. This is
     *                       used for labeling purposes.
     * @param userSchemaName a string representing the name of the user or schema that holds
     *                       the user defined tables with the database. Must not be null
     * @param connection     an active connection to the database, must not be null
     * @param dialect        the SLQ dialect for this type of database, must not null
     */
    public Database(String dbName, String userSchemaName, Connection connection, SQLDialect dialect) {
        Objects.requireNonNull(dbName, "The database name was null");
        Objects.requireNonNull(userSchemaName, "The database user/schema was null");
        Objects.requireNonNull(connection, "The database connection was null");
        Objects.requireNonNull(dialect, "The database dialect was null");
        myName = dbName;
        myConnection = connection;
        mySQLDialect = dialect;
        myUserSchema = getSchema(userSchemaName);
        if (myUserSchema == null) {
            DbLogger.error("The supplied userSchema name {} was not in the database.", userSchemaName);
            throw new DataAccessException("The supplied userSchema name was not in the database: " + userSchemaName);
        }
        turnOffJooQDefaultExecutionLogging();
    }

    /**
     *
     * @return a connection to the database
     */
    public final Connection getConnection(){
        return myConnection;
    }

    /**
     * @return a label or name for the database
     */
    public final String getName() {
        return myName;
    }



    /**
     * @return the sql dialect for the database.  Here should be derby
     */
    public final SQLDialect getSQLDialect() {
        return mySQLDialect;
    }


    /**
     * The schema that holds the user defined tables within the database.
     *
     * @return the user defined schema (as opposed to the system defined schema)
     */
    public Schema getUserSchema() {
        return myUserSchema;
    }

    /**
     * @return the jooq DSLContext for the database
     */
    public DSLContext getDSLContext() {
        if (myExecuteLoggingSettings == null) {
            return DSL.using(getConnection(), getSQLDialect());
        } else {
            return DSL.using(getConnection(), getSQLDialect(), myExecuteLoggingSettings);
        }
    }

    /**
     * Turns on JooQ Default execute SQL logging
     */
    public final void turnOffJooQDefaultExecutionLogging() {
        myExecuteLoggingSettings = new Settings().withExecuteLogging(false);
    }

    /**
     * Turns off JooQ Default execute SQL logging
     */
    public final void turnOnJooQDefaultExecutionLogging() {
        myExecuteLoggingSettings = null;
    }


}