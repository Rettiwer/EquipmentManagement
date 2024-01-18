package com.rettiwer.equipmentmanagement.jwt;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MockAccessToken {}