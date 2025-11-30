package com.poneres.portal.security.auth0;

import com.poneres.portal.user.Role;
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
import java.util.Arrays;
import java.util.List;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class UserAuthorizedAspect {

    @Autowired
    private AuthenticationService authenticationService;

    @Before("@annotation(com.poneres.portal.security.auth0.UserAuthorized)")
    public void check(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserAuthorized userAuthorized = method.getAnnotation(UserAuthorized.class);

        List<String> value = Arrays.asList(userAuthorized.value());
        if (value.contains("isAdmin") && authenticationService.hasRole(Role.ADMIN)) {
            return;
        }

        throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED);
    }
}
