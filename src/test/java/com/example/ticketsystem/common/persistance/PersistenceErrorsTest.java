package com.example.ticketsystem.common.persistance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.PersistenceException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PersistenceErrorsTest {
    @Test
    void detectsUniqueViolationDeepInCauseChain() {
        SQLException sqlError = new SQLException("duplicate key", "23505");
        PersistenceException error = new PersistenceException(new RuntimeException(sqlError));

        assertTrue(PersistenceErrors.isUniqueViolation(error));
    }

    @Test
    void ignoresOtherConstraintViolations() {
        SQLException foreignKeyError = new SQLException("foreign key violation", "23503");

        assertFalse(PersistenceErrors.isUniqueViolation(new PersistenceException(foreignKeyError)));
    }

    @Test
    void returnsFalseWithoutSqlException() {
        assertFalse(PersistenceErrors.isUniqueViolation(new PersistenceException("no cause")));
        assertFalse(PersistenceErrors.isUniqueViolation(null));
    }

}