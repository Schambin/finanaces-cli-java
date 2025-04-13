package main.java.service;

import main.java.model.Account;
import main.java.model.AccountType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccountService {
    private final List<Account> accounts = new ArrayList<>();

    public Account addAccount(String description, double value, LocalDate dueDate, AccountType type) {
        if(value <= 0){
            throw new IllegalArgumentException("Value must be positive.");
        }

        if(description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cant be empty.");
        }

        Account account = new Account(description, value, dueDate, type);
        accounts.add(account);
        return account;
    }

    public void markAsPaid(String inputId) {
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
