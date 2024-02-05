package com.rettiwer.equipmentmanagement.api.jwt;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockAccessToken {
}