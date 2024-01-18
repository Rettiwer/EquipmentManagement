package com.rettiwer.equipmentmanagement.jwt;

import com.rettiwer.equipmentmanagement.authentication.AuthenticationRequest;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.Role;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.ModifierSupport;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.util.function.Predicate;


public class MockAccessTokenExtension implements BeforeEachCallback {
    private String newToken;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Class<?> testClass = context.getRequiredTestClass();
        Object testInstance = context.getRequiredTestInstance();

        newToken = generateNewAccessToken(context);

        injectFields(testClass, testInstance, ModifierSupport::isNotStatic);
    }

    private void injectFields(Class<?> testClass, Object testInstance,
                              Predicate<Field> predicate) throws IllegalAccessException {

        for (Field field : testClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(MockAccessToken.class))
                FieldUtils.writeField(testInstance, field.getName(), newToken, true);
        }
    }

    private String generateNewAccessToken(ExtensionContext context) {
        AuthenticationService service = SpringExtension.getApplicationContext(context).getBean(AuthenticationService.class);
        service.register(new RegisterRequest(
                "Api",
                "Test",
                "someone@example.com",
                "SecretPass!",
                Role.ADMIN
        ));

        return service.authenticate(new AuthenticationRequest(
                "someone@example.com",
                "SecretPass!"
        )).getAccessToken();

    }
}
