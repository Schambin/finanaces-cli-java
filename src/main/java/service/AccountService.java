package main.java.service;

import main.java.model.Account;
import main.java.model.AccountType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AccountService {
    private final List<Account> accounts = new ArrayList<>();

    public Account addAccount(String description, double value, LocalDate dueDate, AccountType type) {
        validateAccountInput(description, value);
        Account account = new Account(description, value, dueDate, type);
        accounts.add(account);
        return account;
    }

    private void validateAccountInput(String description, double value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description can't be empty.");
        }
    }

    // status mark methods
    public void markAsPaid(String inputId) {
        updatePaymentStatus(inputId, AccountType.PAYABLE);
    }

    public void markAsReceived(String inputId) {
        updatePaymentStatus(inputId, AccountType.RECEIVABLE);
    }

    private void updatePaymentStatus(String inputId, AccountType expectedType) {
        Account account = findAccountById(inputId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getType() != expectedType) {
            throw new IllegalArgumentException(
                    String.format("This is a %s account, expected %s",
                            account.getType(), expectedType));
        }

        account.setPaid(true);
    }

    // search methods
    public Optional<Account> findAccountById(String id) {
        try {
            int sequentialId = Integer.parseInt(inputId);
            getNumberedPendingAccounts().get(sequentialId).setPaid(true);
        } catch (NumberFormatException e) {
            UUID accountId = UUID.fromString(inputId);
            getAccountById(accountId).ifPresent(account -> account.setPaid(true));
        }
    }

    public void markAsReceived(String inputId) {
        try {
            int sequentialId = Integer.parseInt(inputId);
            getNumberedPendingReceivables().get(sequentialId).setPaid(true);
        } catch (NumberFormatException e) {
            UUID accountId = UUID.fromString(inputId);
            getAccountById(accountId).ifPresent(account -> account.setPaid(true));
        }
    }

    public Map<Integer, Account> getNumberedPendingPayables() {
        return getNumberedAccounts(account ->
                !account.isPaid() && account.getType() == AccountType.PAYABLE);
    }

    public Map<Integer, Account> getNumberedPendingReceivables() {
        return getNumberedAccounts(account ->
                !account.isPaid() && account.getType() == AccountType.RECEIVABLE);
    }

    public Map<Integer, Account> getNumberedPaidAccounts() {
        return getNumberedAccounts(Account::isPaid);
    }

    private Map<Integer, Account> getNumberedAccounts(Predicate<Account> filter) {
        Map<Integer, Account> numberedAccounts = new LinkedHashMap<>();
        int counter = 1;

        for (Account account : accounts) {
            if (filter.test(account)) {
                numberedAccounts.put(counter++, account);
            }
        }

        return numberedAccounts;
    }

    public Map<Integer, Account> getNumberedPaidPayables() {
        return getNumberedAccounts(account ->
                account.isPaid() && account.getType() == AccountType.PAYABLE);
    }

    public Map<Integer, Account> getNumberedPaidReceivables() {
        return getNumberedAccounts(account ->
                account.isPaid() && account.getType() == AccountType.RECEIVABLE);
    }

    public Optional<Account> getAccountById(UUID accountId) {
        return accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst();
    }

    // aux methods
    public void loadSampleData() {
        addAccount("Aluguel", 1500.00, LocalDate.now().plusDays(30), AccountType.PAYABLE);
        addAccount("Salário", 5000.0, LocalDate.now().plusDays(5), AccountType.RECEIVABLE);
        addAccount("Internet", 120.9, LocalDate.now().minusDays(10), AccountType.PAYABLE);

        accounts.stream()
                .filter(account -> account.getDescription().equals("Internet"))
                .findFirst()
                .ifPresent(account -> account.setPaid(true));
    }

    public Optional<Account> getAccountById(UUID accountId) {
        return accounts.stream().filter(
        account -> account.getId().equals(accountId)).findFirst();
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public List<Account> getAccountsByType(AccountType type) {
        return accounts.stream()
            .filter(account -> account.getType() == type).collect(Collectors.toList());
    }

    public List<Account> getPendingAccounts() {
        return accounts.stream()
            .filter(account -> !account.isPaid()).collect(Collectors.toList());
    }

    public List<Account> getOverdueAccounts() {
        LocalDate today = LocalDate.now();
        return accounts.stream()
                .filter(account -> !account.isPaid())
                .filter(account -> account.getDueDate().isBefore(today))
                .collect(Collectors.toList());
    }

    public String getAccountStatus(UUID accountId) {
        return getAccountById(accountId).map(account -> {
            if(account.isPaid()) {
                return "Paid";
            } else if(account.getDueDate().isBefore(LocalDate.now())){
                return "Overdue";
            }
            return "Pending";
        }).orElse("Account not found.");
    }

    public Map<Integer, Account> getNumberedPendingAccounts() {
        Map<Integer, Account> numberedAccounts = new LinkedHashMap<>();
        int counter = 1;
        for (Account account : accounts) {
            if (!account.isPaid()) {
                numberedAccounts.put(counter++, account);
            }
        }
        return numberedAccounts;
    }

    public Map<Integer, Account> getNumberedPendingPayables() {
        Map<Integer, Account> numberedAccounts = new LinkedHashMap<>();
        int counter = 1;
        for (Account account : accounts) {
            if (!account.isPaid() && account.getType() == AccountType.PAYABLE) {
                numberedAccounts.put(counter++, account);
            }
        }
        return numberedAccounts;
    }

    public Map<Integer, Account> getNumberedPendingReceivables() {
        Map<Integer, Account> numberedAccounts = new LinkedHashMap<>();
        int counter = 1;
        for (Account account : accounts) {
            if (!account.isPaid() && account.getType() == AccountType.RECEIVABLE) {
                numberedAccounts.put(counter++, account);
            }
        }
        return numberedAccounts;
    }
}
