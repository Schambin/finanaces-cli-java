package main.java.service;

import main.java.model.Account;
import main.java.model.AccountType;

import java.util.List;

public class SummaryService {
    private final AccountService accountService;

    public SummaryService(AccountService accountService) {
        this.accountService = accountService;
    }

    public double getTotalPayable() {
        return calculateTotal(accountService.getAccountsByType(AccountType.PAYABLE));
    }

    public double getTotalReceivable() {
        return calculateTotal(accountService.getAccountsByType(AccountType.RECEIVABLE));
    }

    public double getNetBalance() {
        return getTotalReceivable() - getTotalPayable();
    }

    public double getPendingTotalByType(AccountType type) {
        return calculateTotal(accountService
                .getPendingAccounts()
                .stream()
                .filter(account -> account.getType() == type)
                .toList());
    }

    public double getOverdueTotalByType(AccountType type) {
        return calculateTotal(accountService
                .getOverdueAccounts()
                .stream()
                .filter(account -> account.getType() == type)
                .toList());
    }

    private double calculateTotal(List<Account> accounts) {
        return accounts.stream().mapToDouble(Account::getValue).sum();
    }

    public String generateFullReport() {
        StringBuilder report = new StringBuilder();

        report.append("\n=== Financial Resume ===\n");
        report.append(String.format("Total to Pay: R$ %.2f%n", getTotalPayable()));
        report.append(String.format("Total to Receive: R$ %.2f%n", getTotalReceivable()));
        report.append(String.format("Net Balance: R$ %.2f%n", getNetBalance()));

        report.append("\n--- Detailing ---\n");
        report.append(String.format("Pending Pays: R$ %.2f%n", getPendingTotalByType(AccountType.PAYABLE)));
        report.append(String.format("Pending Receives: R$ %.2f%n", getPendingTotalByType(AccountType.RECEIVABLE)));
        report.append(String.format("Pay Overdue: R$ %.2f%n", getOverdueTotalByType(AccountType.PAYABLE)));

        return report.toString();
    }
}
