package com.rettiwer.equipmentmanagement.invoice;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping(path = "/invoice")
public class InvoiceController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> index() {
        Map<String, String> json = new HashMap<>();
        json.put("message", "Hello world");
        return json;
    }

    @PostMapping
    public ResponseEntity<String> create() {
        return ResponseEntity.ok("SUCCESS");
    }
}
