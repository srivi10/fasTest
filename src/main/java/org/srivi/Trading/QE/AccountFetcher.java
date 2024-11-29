package org.srivi.Trading.QE;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class AccountFetcher {
    private Map<String, String[]> accounts;

    public AccountFetcher(Map<String, String[]> accounts) {
        this.accounts = accounts;
    }

    public List<String[]> fetchAccounts(Map<String, String> criteria) {
        List<String[]> matchedAccounts = new ArrayList<>();

        for (Map.Entry<String, String[]> entry : accounts.entrySet()) {
            String[] accountDetails = entry.getValue();
            boolean match = true;

            for (Map.Entry<String, String> criterion : criteria.entrySet()) {
                String criterionKey = criterion.getKey();
                String criterionValue = criterion.getValue();

                if (!"Select".equals(criterionValue)) {
                    int index = getCriteriaIndex(criterionKey);
                    if (index != -1 && !accountDetails[index].equalsIgnoreCase(criterionValue)) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                matchedAccounts.add(accountDetails);
            }
        }

        return matchedAccounts;
    }

    private int getCriteriaIndex(String criterion) {
        switch (criterion) {
            case "Account Holder":
                return 2; // Index for account holder
            case "Account Type":
                return 3; // Index for account type
            case "Transfer":
                return 4; // Index for transfer eligibility
            case "Offers":
                return 5; // Index for offers eligibility
            case "Payment Plan":
                return 6; // Index for payment plan
            case "Credit Limit":
                return 7; // Index for credit limit (new addition)
            default:
                return -1;
        }
    }
}
