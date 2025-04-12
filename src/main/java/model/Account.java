package main.java.model;

import java.time.LocalDate;
import java.util.UUID;

public class Account {
    private final UUID id;
    private final String description;
    private final double value;
    private final LocalDate dueDate;
    private final AccountType type;
    private boolean isPaid;

    public Account(String description, double value, LocalDate dueDate, AccountType type) {
        this.id = UUID.randomUUID();
        this.description = description;
        this.value = value;
        this.dueDate = dueDate;
        this.type = type;
        this.isPaid = false;
    }

    // ===== GETTERS =====
    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getValue() {
        return value;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public AccountType getType() {
        return type;
    }

    public boolean isPaid() {
        return isPaid;
    }

    // ===== SETTERS =====
    public void setPaid(boolean paid) {
        if(this.isPaid && !paid) {
            throw new IllegalStateException("Account already paid!");
        }
        this.isPaid = paid;
    }
}
