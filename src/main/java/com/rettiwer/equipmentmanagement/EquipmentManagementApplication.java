package com.rettiwer.equipmentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@Controller
@SpringBootApplication
public class EquipmentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquipmentManagementApplication.class, args);
    }

    @GetMapping
    public String redirectToEquipment() {
        return "redirect:/equipment";
    }

}
