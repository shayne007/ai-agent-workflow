package com.fintech.aiagent.domain.customer.valueobject;

import java.util.Objects;

/**
 * Value object representing a unique identifier for a customer.
 */
public class CustomerId {
    private final String id;

    public CustomerId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}