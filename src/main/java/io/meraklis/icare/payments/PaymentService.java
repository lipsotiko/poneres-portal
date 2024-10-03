package io.meraklis.icare.payments;

import io.meraklis.icare.user.SignUp;
import io.meraklis.icare.user.UserProfile;

public interface PaymentService {
    String createAccount(SignUp signUp);

    PaymentAccount getAccount();

    PaymentAccountLink getAccountLink();

    void deleteAccount(UserProfile userProfile);
}
