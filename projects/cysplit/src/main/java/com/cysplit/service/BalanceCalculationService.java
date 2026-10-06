package com.cysplit.service;

import com.cysplit.model.Expense;
import com.cysplit.model.Split;
import com.cysplit.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BalanceCalculationService {

    public static class DebtTransfer {
        private String fromUser;
        private String toUser;
        private BigDecimal amount;

        public DebtTransfer(String fromUser, String toUser, BigDecimal amount) {
            this.fromUser = fromUser;
            this.toUser = toUser;
            this.amount = amount;
        }

        public String getFromUser() { return fromUser; }
        public String getToUser() { return toUser; }
        public BigDecimal getAmount() { return amount; }
    }

    /**
     * Calculates net balances for each user from a list of expenses.
     * Positive balance = user is owed money.
     * Negative balance = user owes money.
     */
    public Map<String, BigDecimal> calculateNetBalances(List<Expense> expenses) {
        Map<String, BigDecimal> netBalances = new HashMap<>();

        for (Expense expense : expenses) {
            String payer = expense.getPayer().getName();
            BigDecimal totalAmount = expense.getTotalAmount();

            netBalances.put(payer, netBalances.getOrDefault(payer, BigDecimal.ZERO).add(totalAmount));

            for (Split split : expense.getSplits()) {
                String participant = split.getUser().getName();
                BigDecimal splitAmount = split.getAmount();

                netBalances.put(participant, netBalances.getOrDefault(participant, BigDecimal.ZERO).subtract(splitAmount));
            }
        }
        return netBalances;
    }

    /**
     * Simplifies debts into minimum number of transactions using a greedy algorithm.
     */
    public List<DebtTransfer> simplifyDebts(Map<String, BigDecimal> netBalances) {
        List<DebtTransfer> transfers = new ArrayList<>();

        // Priority queues for debtors (negative net balance) and creditors (positive net balance)
        PriorityQueue<Map.Entry<String, BigDecimal>> debtors = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));
        PriorityQueue<Map.Entry<String, BigDecimal>> creditors = new PriorityQueue<>((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, BigDecimal> entry : netBalances.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            } else if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<String, BigDecimal> debtor = debtors.poll();
            Map.Entry<String, BigDecimal> creditor = creditors.poll();

            BigDecimal debtAmount = debtor.getValue().abs();
            BigDecimal creditAmount = creditor.getValue();

            BigDecimal minAmount = debtAmount.min(creditAmount).setScale(2, RoundingMode.HALF_UP);
            transfers.add(new DebtTransfer(debtor.getKey(), creditor.getKey(), minAmount));

            BigDecimal remainingDebt = debtAmount.subtract(minAmount);
            BigDecimal remainingCredit = creditAmount.subtract(minAmount);

            if (remainingDebt.compareTo(BigDecimal.ZERO) > 0) {
                debtors.add(new AbstractMap.SimpleEntry<>(debtor.getKey(), remainingDebt.negate()));
            }

            if (remainingCredit.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new AbstractMap.SimpleEntry<>(creditor.getKey(), remainingCredit));
            }
        }

        return transfers;
    }
}
