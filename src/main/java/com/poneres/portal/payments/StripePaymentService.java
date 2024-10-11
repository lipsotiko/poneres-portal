package com.poneres.portal.payments;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountCreateParams.BusinessProfile;
import com.stripe.param.AccountCreateParams.Type;
import com.stripe.param.AccountLinkCreateParams;
import com.poneres.portal.security.auth0.AuthenticationService;
import com.poneres.portal.user.SignUp;
import com.poneres.portal.user.UserProfile;
import com.poneres.portal.user.UserProfileRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.stripe.param.AccountCreateParams.Capabilities.*;

@Service
public class StripePaymentService implements PaymentService {

    @Value("${stripe.private-key}")
    private String apiKey;

    @Value("${webapp.url}")
    private String webappUrl;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PostConstruct
    public void runAfterCreated() {
        Stripe.apiKey = apiKey;
    }

    @Override
    public String createAccount(SignUp signUp) {
        AccountCreateParams params = AccountCreateParams
                .builder()
                .setCountry("US")
                .setType(Type.EXPRESS)
                .setBusinessType("individual") // company, government_entity, individual, non_profit
                .setIndividual(AccountCreateParams.Individual.builder()
                        .setFirstName(signUp.getFirstName())
                        .setLastName(signUp.getLastName())
                        .build())
                .setEmail(signUp.getEmail())
                .setCapabilities(builder()
                        .setCardPayments(CardPayments.builder().setRequested(true).build())
                        .setTransfers(Transfers.builder().setRequested(true).build())
                        .setUsBankAccountAchPayments(UsBankAccountAchPayments.builder().setRequested(true).build())
                        .build())
                .setBusinessProfile(
                        BusinessProfile.builder().setMcc("6513").setProductDescription("Property management").build()
                ).build();
        try {
            UserProfile userProfile = userProfileRepository.findByEmail(signUp.getEmail());
            Account account = Account.create(params);
            userProfile.setPaymentProviderId(account.getId());
            userProfileRepository.save(userProfile);
            return account.getId();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentAccount getAccount() {
        Account account = getStripeAccount();
        Set<String> requirementsDue = new HashSet<>();
        requirementsDue.addAll(account.getRequirements().getEventuallyDue());
        requirementsDue.addAll(account.getRequirements().getCurrentlyDue());
        requirementsDue.addAll(account.getRequirements().getPastDue());
        List<String> pendingVerification = account.getRequirements().getPendingVerification();
        return PaymentAccount.builder()
                .pendingVerification(!pendingVerification.isEmpty())
                .requirementsDue(requirementsDue)
                .build();
    }

    @Override
    public PaymentAccountLink getAccountLink() {
        try {
            UserProfile userProfile = authenticationService.getUserProfile();
            AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                    .setAccount(userProfile.getPaymentProviderId())
                    .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                    .setRefreshUrl(webappUrl + "/earnings/payout-method")
                    .setReturnUrl(webappUrl + "/earnings/payout-method")
                    .build();
            AccountLink paymentAccountLink = AccountLink.create(params);

            return PaymentAccountLink.builder()
                    .url(paymentAccountLink.getUrl())
                    .expiresAt(paymentAccountLink.getExpiresAt())
                    .build();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAccount(UserProfile userProfile) {
        if (userProfile.getPaymentProviderId() == null) {
            return;
        }
        try {
            Account.retrieve(userProfile.getPaymentProviderId()).delete();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private Account getStripeAccount() {
        UserProfile userProfile = authenticationService.getUserProfile();
        try {
            return Account.retrieve(userProfile.getPaymentProviderId());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public void deleteAccount(String accountId) {
//        try {
//            Account.retrieve(accountId).delete();
//        } catch (StripeException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Override
//    public String createProduct(String description, String accountId) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", description);
//        try {
//            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(accountId).build();
//            return Product.create(params, requestOptions).getId();
//        } catch (StripeException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
