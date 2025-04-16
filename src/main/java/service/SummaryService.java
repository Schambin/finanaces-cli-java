package main.java.service;

import main.java.model.Account;
import main.java.model.AccountType;

import java.util.List;
import java.util.stream.StreamSupport;

public class SummaryService {
    private final AccountService accountService;

    public SummaryService(AccountService accountService) {
        this.accountService = accountService;
    }

    public double getTotalPayable() {
        return calculateTotal(accountService.getNumberedAccountsByType(AccountType.PAYABLE).values());
    }

    public double getTotalReceivable() {
        return calculateTotal(accountService.getNumberedAccountsByType(AccountType.RECEIVABLE).values());
    }

    public double getNetBalance() {
        return getTotalReceivable() - getTotalPayable();
    }

    public double getPendingTotalByType(AccountType type) {
        if (type == AccountType.PAYABLE) {
            return calculateTotal(accountService.getNumberedPendingPayables().values());
        } else {
            return calculateTotal(accountService.getNumberedPendingReceivables().values());
        }
    }

    public double getOverdueTotalByType(AccountType type) {
        return calculateTotal(accountService.getOverdueAccounts().stream()
                .filter(account -> account.getType() == type)
                .toList());
    }

    public String generateFullReport() {
        StringBuilder report = new StringBuilder();

        report.append("\n=== Financial Resume ===\n");
        report.append(String.format("Payed Total: R$ %.2f%n", getTotalPayable()));
        report.append(String.format("Received Total: R$ %.2f%n", getTotalReceivable()));
        report.append(String.format("Net Balance: R$ %.2f%n", getNetBalance()));

        report.append("\n--- Detailing ---\n");
        report.append(String.format("Pending Pays: R$ %.2f%n", getPendingTotalByType(AccountType.PAYABLE)));
        report.append(String.format("Pending Receives: R$ %.2f%n", getPendingTotalByType(AccountType.RECEIVABLE)));
        report.append(String.format("Pay Overdue: R$ %.2f%n", getOverdueTotalByType(AccountType.PAYABLE)));

        return report.toString();
    }

    private double calculateTotal(Iterable<Account> accounts) {
        return StreamSupport.stream(accounts.spliterator(), false)
                .mapToDouble(Account::getValue)
                .sum();
    }
}
