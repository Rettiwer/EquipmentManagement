package com.rettiwer.equipmentmanagement.equipment;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> index() {
        Map<String, String> json = new HashMap<>();
        json.put("message", "Hello world");
        return json;
    }
}
