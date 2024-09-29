package io.meraklis.icare.payments;

public interface PaymentService {
    String createAccount(String email);

    PaymentAccount getAccount(String email);

    PaymentAccountLink getAccountLink(String email);
//    void deleteAccount(String accountId);
//    String createProduct(String description, String accountId);
}
