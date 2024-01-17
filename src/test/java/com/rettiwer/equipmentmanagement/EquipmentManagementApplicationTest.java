package com.rettiwer.equipmentmanagement;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EquipmentManagementApplicationTest {
    @LocalServerPort
    int port;
    public static String API_ROOT;

    public static String ACCESS_TOKEN;

    @Test
    public void contextLoads() {
        buildApiUrl();
    }

    private void buildApiUrl() {
        API_ROOT = "http://127.0.0.1:" + port + "/api";
    }
}
