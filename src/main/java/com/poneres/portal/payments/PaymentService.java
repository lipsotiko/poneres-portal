package com.poneres.portal.payments;

import com.poneres.portal.user.SignUp;
import com.poneres.portal.user.UserProfile;

public interface PaymentService {
    String createAccount(SignUp signUp);

    PaymentAccount getAccount();

    PaymentAccountLink getAccountLink();

    void deleteAccount(UserProfile userProfile);
}
