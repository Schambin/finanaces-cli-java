package main.java.model;

public enum AccountType {
    PAYABLE("Pay"),
    RECEIVABLE("Receive");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
