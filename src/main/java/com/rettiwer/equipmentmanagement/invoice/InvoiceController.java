package com.rettiwer.equipmentmanagement.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping()
    public ResponseEntity<?> index() {
        return new ResponseEntity<>(invoiceService.getAll(), HttpStatus.OK);
    }
    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody InvoiceDTO request) {
        return new ResponseEntity<>(invoiceService.store(request), HttpStatus.CREATED);
    }
}
