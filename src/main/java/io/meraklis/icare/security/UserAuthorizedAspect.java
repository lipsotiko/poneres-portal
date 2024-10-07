package io.meraklis.icare.security;

import io.meraklis.icare.applications.PatientApplicationRepository;
import io.meraklis.icare.documents.PatientDocumentRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.lang.reflect.Method;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class UserAuthorizedAspect {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PatientApplicationRepository applicationRepository;

    @Autowired
    private PatientDocumentRepository documentRepository;

    @Before("@annotation(io.meraklis.icare.security.UserAuthorized)")
    public void check(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserAuthorized userAuthorized = method.getAnnotation(UserAuthorized.class);

        String value = userAuthorized.value();
        if (value.equals("applicationId")) {
            String applicationId = (String) joinPoint.getArgs()[userAuthorized.position()];
            authorizedForApplication(applicationId);
            return;
        }

        if (value.equals("documentId")) {
            String documentId = (String) joinPoint.getArgs()[userAuthorized.position()];
            documentRepository.findById(documentId).ifPresent(document -> authorizedForApplication(document.getApplicationId()));
        }
    }

    private void authorizedForApplication(String applicationId) {
        applicationRepository.findById(applicationId).ifPresent(application -> {
            if (!authenticationService.isAuthorized(application)) {
                throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED);
            }
        });
    }
}
