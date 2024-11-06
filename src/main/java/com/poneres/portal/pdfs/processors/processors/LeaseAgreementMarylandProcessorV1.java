package com.poneres.portal.pdfs.processors.processors;

import com.poneres.portal.agreements.SignatureRecipient;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class LeaseAgreementMarylandProcessorV1 extends AbstractProcessor {

    @Override
    public void preProcess(Map<String, Object> metadata) {
        LocalDate startDate = setDateField(metadata, "startDate", "startDay", "startMonthYear", false);
        LocalDate endDate = setDateField(metadata, "endDate", "endDay", "endMonthYear", true);

        setLeaseTerm(metadata, startDate, endDate);

        Money monthlyRent = setMonthlyRentInDollars(metadata);
        if (monthlyRent != null) {
            metadata.put("monthlyRent", moneyToWords(monthlyRent));
        }

        Money totalRent = totalRent(metadata, startDate, endDate, monthlyRent);
        if (totalRent != null) {
            metadata.put("totalRent", moneyToWords(totalRent));
        }

        Money securityDeposit = securityDeposit(metadata);
        if (securityDeposit != null) {
            metadata.put("securityDeposit", moneyToWords(securityDeposit));
        }

        Money proRatedRent = proRatedRent(metadata);
        if (proRatedRent != null) {
            metadata.put("proRatedRent", moneyToWords(proRatedRent));
        }

        if (metadata.containsKey("dueWithinDaysNumber")) {
            int dueWithinDaysNumber = Integer.parseInt(metadata.get("dueWithinDaysNumber").toString());
            String dueWithinDays = NumberToWords.convert(dueWithinDaysNumber);
            metadata.put("dueWithinDays", dueWithinDays);
        }

        putDollars(metadata, "lateFeeInDollars", "lateFeeNumber");
        putDollars(metadata, "petDepositInDollars", "petDeposit");

        yesNo(metadata, "sendNoticeTo", "isLandlord", "isPropertyManager");

        yesNo(metadata, "authorizedToHavePets", "petsAllowedYes", "petsAllowedNo");
        yesNo(metadata, "authorizedToHaveServiceAnimal", "serviceAnimalsAllowedYes", "serviceAnimalsAllowedNo");

        yesNo(metadata, "federalAgeClassification", "federalBuiltBefore1978", "federalBuiltDuringAfter1978");
        yesNo(metadata, "stateAgeClassification", "stateBuiltBefore1978", "stateBuiltDuringAfter1978");

        copyTo(metadata, "tenant", "tenant2");
        copyTo(metadata, "landlord", "landlord2");
        copyTo(metadata, "leasedAddress", "leasedAddress2");

        copyTo(metadata, "tenant", "tenant3");
        copyTo(metadata, "landlord", "landlord3");
        copyTo(metadata, "leasedAddress", "leasedAddress3");
    }

    private static void copyTo(Map<String, Object> metadata, String from, String to) {
        if (metadata.containsKey(from)) {
            metadata.put(to, metadata.get(from));
        }
    }

    private void putDollars(Map<String, Object> metadata, String dollarsKey, String putKey) {
        if (metadata.containsKey(dollarsKey)) {
            Double dueWithinDaysNumber = fromObj(metadata.get(dollarsKey));
            if (dueWithinDaysNumber != null) {
                Money lateFee = Money.of(CurrencyUnit.USD, dueWithinDaysNumber);
                metadata.put(putKey, formatMoney(lateFee));
            }
            metadata.remove(dollarsKey);
        }
    }

    private Money securityDeposit(Map<String, Object> metadata) {
        if (metadata.containsKey("securityDepositInDollars")) {
            Double securityDeposit = fromObj(metadata.get("securityDepositInDollars"));
            if (securityDeposit != null) {
                return getMoney(metadata, securityDeposit, "securityDepositNumber", "securityDepositInDollars");
            }
        }

        return null;
    }

    private LocalDate setDateField(Map<String, Object> metadata, String dateField, String dayField, String monthYearField, Boolean endOfMonth) {
        String field = (String) metadata.get(dateField);

        if (field != null) {
            LocalDate date = LocalDate.parse(field + "-01");

            if (endOfMonth) {
                date = date.plusMonths(1).minusDays(1);
            }

            int dayOfMonth = date.getDayOfMonth();
            metadata.put(dayField, dayOfMonth + getOrdinalSuffix(dayOfMonth));
            metadata.put(monthYearField, date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ", " + date.getYear());
            metadata.remove(dateField);
            return date;
        }
        return null;
    }

    private void setLeaseTerm(Map<String, Object> metadata, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            int totalMonths = months(startDate, endDate);

            int years = totalMonths / 12;
            int months = totalMonths % 12;

            String termLength = "";
            termLength = (years > 0) ? years + " Year" : "";
            termLength += (years > 1) ? "s" : "";

            termLength += (months > 0) ? " " + months + " Month" : "";
            termLength += (months > 1) ? "s" : "";

            metadata.put("termLength", termLength.trim());
        }
    }

    private Money setMonthlyRentInDollars(Map<String, Object> metadata) {
        if (metadata.containsKey("monthlyRentInDollars")) {
            Double monthlyRentInDollarsDouble = fromObj(metadata.get("monthlyRentInDollars"));
            if (monthlyRentInDollarsDouble != null) {
                return getMoney(metadata, monthlyRentInDollarsDouble, "monthlyRentNumber", "monthlyRentInDollars");
            }
        }

        return null;
    }

    private Money totalRent(Map<String, Object> metadata, LocalDate startDate, LocalDate endDate, Money monthlyRent) {
        if (startDate != null && endDate != null && monthlyRent != null) {
            int months = months(startDate, endDate);
            Money totalRent = Money.of(CurrencyUnit.USD, monthlyRent.getAmount().multiply(BigDecimal.valueOf(months)));
            metadata.put("totalRentNumber", formatMoney(totalRent));
            return totalRent;
        }

        return null;
    }

    private Money proRatedRent(Map<String, Object> metadata) {
        if (metadata.containsKey("proRatedRentInDollars")) {
            metadata.get("proRatedRentInDollars");
            Double proRatedRentInDollarsDouble = fromObj(metadata.get("proRatedRentInDollars"));
            if (proRatedRentInDollarsDouble != null) {
                return getMoney(metadata, proRatedRentInDollarsDouble, "proRatedRentNumber", "proRatedRentInDollars");
            }
            metadata.remove("proRatedRentInDollars");
        }

        return null;
    }

    private int months(LocalDate start, LocalDate end) {
        Period period = Period.between(start, end);
        return period.getYears() * 12 + period.getMonths() + ((period.getDays() > 0) ? 1 : 0);
    }

    private Money getMoney(Map<String, Object> metadata, Double doubleDollars, String putKey, String deleteKey) {
        Money money = Money.of(CurrencyUnit.USD, doubleDollars);
        metadata.put(putKey, formatMoney(money));
        metadata.remove(deleteKey);
        return money;
    }

    private String formatMoney(Money money) {
        MoneyFormatter formatter = new MoneyFormatterBuilder().appendAmount().toFormatter();
        return formatter.print(money);
    }

    private Double fromObj(Object obj) {
        if (obj instanceof Integer) {
            return Double.parseDouble(Integer.toString((int) obj));
        } else if (obj instanceof String) {
            String s = String.valueOf(obj);
            if (s.isEmpty()) {
                return null;
            }
            return Double.parseDouble(s);
        } else {
            return null;
        }
    }

    private String getOrdinalSuffix(int num) {
        if (num > 3 && num < 21) return "th";
        return switch (num % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    private String moneyToWords(Money money) {
        BigDecimal remainder = money.getAmount().remainder(BigDecimal.ONE);
        BigDecimal cents = remainder.multiply(BigDecimal.valueOf(100L));

        String words = NumberToWords.convert(money.getAmount().longValue());

        if (cents.longValue() > 0) {
            words += " " + cents.longValue() + "/100";
        }

        return words;
    }

    @Override
    public List<Map<String, Object>> signatureFields(List<SignatureRecipient> recipients) {
        List<Map<String, Object>> fields = new ArrayList<>();
        List<Integer> initialBottomOfPages = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 17);

        int multiTenantInitialsGap = 0;
        int page12SignaturePosition = 932;
        int page14SignaturePosition = 640;
        int page15SignaturePosition = 542;
        int page16SignaturePosition = 914;

        for (int i = 0; i < recipients.size(); i++) {
            if (i == 0) {
                for (Integer page : initialBottomOfPages) {
                    fields.add(buildField(page, 730, 1044, i, "initials"));
                }
            } else {
                for (Integer page : initialBottomOfPages) {
                    fields.add(buildField(page, 36 + multiTenantInitialsGap, 1044, i, "initials"));
                }

                fields.add(buildField(4, 86 + multiTenantInitialsGap, 244, i, "initials"));
                fields.add(buildField(5, 86 + multiTenantInitialsGap, 619, i, "initials"));
                fields.add(buildField(6, 86 + multiTenantInitialsGap, 777, i, "initials"));
                fields.add(buildField(9, 86 + multiTenantInitialsGap, 84, i, "initials"));
                fields.add(buildField(10, 118 + multiTenantInitialsGap, 101, i, "initials"));
                fields.add(buildField(10, 86 + multiTenantInitialsGap, 623, i, "initials"));
                multiTenantInitialsGap += 54;
            }

            fields.add(buildField(12, 72, page12SignaturePosition, i, "signature"));
            page12SignaturePosition -= 50;

            fields.add(buildField(14, 72, page14SignaturePosition, i, "signature"));
            page14SignaturePosition -= 50;

            fields.add(buildField(15, 72, page15SignaturePosition, i, "signature"));
            page15SignaturePosition -= 50;

            fields.add(buildField(16, 72, page16SignaturePosition, i, "signature"));
            page16SignaturePosition -= 50;
        }

        return fields;
    }
}
