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

        String report = "\n=== FINANCIAL SUMMARY ===\n" +
                String.format("Total to Pay: R$ %.2f%n", getTotalPayable()) +
                String.format("Total to Receive: R$ %.2f%n", getTotalReceivable()) +
                String.format("Net Balance: R$ %.2f%n", getNetBalance()) +
                "\n--- Details ---\n" +
                String.format("Pending Payables: R$ %.2f%n", getPendingTotalByType(AccountType.PAYABLE)) +
                String.format("Pending Receivables: R$ %.2f%n", getPendingTotalByType(AccountType.RECEIVABLE)) +
                String.format("Overdue Payables: R$ %.2f%n", getOverdueTotalByType(AccountType.PAYABLE));

        return report;
    }

    private double calculateTotal(Iterable<Account> accounts) {
        return StreamSupport.stream(accounts.spliterator(), false)
                .mapToDouble(Account::getValue)
                .sum();
    }
}
