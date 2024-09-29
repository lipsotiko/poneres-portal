package io.meraklis.icare.payments;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountCreateParams.BusinessProfile;
import com.stripe.param.AccountCreateParams.Type;
import com.stripe.param.AccountLinkCreateParams;
import io.meraklis.icare.security.AuthenticationService;
import io.meraklis.icare.user.UserProfile;
import io.meraklis.icare.user.UserProfileRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    // https://github.com/lipsotiko/HappyTenant/blob/master/server/src/main/java/io/meraklis/happy_tenant/payment/StripePaymentService.java
    @Override
    public String createAccount(String email) {
        AccountCreateParams params = AccountCreateParams
                .builder()
                .setCountry("US")
                .setType(Type.EXPRESS)
                .setEmail(email)
                .setCapabilities(builder()
                        .setCardPayments(CardPayments.builder().setRequested(true).build())
                        .setTransfers(Transfers.builder().setRequested(true).build())
                        .setUsBankAccountAchPayments(UsBankAccountAchPayments.builder().setRequested(true).build())
                        .build())
                .setBusinessProfile(
                        BusinessProfile.builder().setMcc("6513").setProductDescription("Property management").build()
                ).build();

        try {
            UserProfile userProfile = authenticationService.getUserProfile();
            Account account = Account.create(params);
            userProfile.setPaymentProviderId(account.getId());
            userProfileRepository.save(userProfile);
            return account.getId();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentAccount getAccount(String email) {
        try {
            UserProfile userProfile = authenticationService.getUserProfile();
            Account retrieve = Account.retrieve(userProfile.getPaymentProviderId());

            Set<String> requirementsDue = new HashSet<>();
            requirementsDue.addAll(retrieve.getRequirements().getEventuallyDue());
            requirementsDue.addAll(retrieve.getRequirements().getCurrentlyDue());
            requirementsDue.addAll(retrieve.getRequirements().getPastDue());

            List<String> pendingVerification = retrieve.getRequirements().getPendingVerification();

            return PaymentAccount.builder()
                    .pendingVerification(!pendingVerification.isEmpty())
                    .requirementsDue(requirementsDue)
                    .build();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentAccountLink getAccountLink(String email) {
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
