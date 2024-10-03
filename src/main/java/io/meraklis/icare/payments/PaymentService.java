package io.meraklis.icare.payments;

import io.meraklis.icare.user.SignUp;

public interface PaymentService {
    String createAccount(SignUp signUp);

    PaymentAccount getAccount();

    PaymentAccountLink getAccountLink();
}
