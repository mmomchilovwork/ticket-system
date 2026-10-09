package com.example.ticketsystem.common.persistance;

import java.sql.SQLException;

public class PersistenceErrors {
    private static final String UNIQUE_VIOLATION_SQL_STATE = "23505";

    private PersistenceErrors() {
    }

    public static boolean isUniqueViolation(Throwable error) {
        for (Throwable t = error; t != null; t = t.getCause()) {
            if (t instanceof SQLException
                    && UNIQUE_VIOLATION_SQL_STATE.equals(((SQLException) t).getSQLState())) {
                return true;
            }
        }
        return false;
    }
}
