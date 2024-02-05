package com.rettiwer.equipmentmanagement.invoice;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> single(@PathVariable("id") Long invoiceId) {
        return new ResponseEntity<>(invoiceService.getById(invoiceId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody InvoiceItemsDTO request) {
        return new ResponseEntity<>(invoiceService.create(request), HttpStatus.CREATED);
    }

    @PutMapping(value =  "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody InvoiceItemsDTO request, @PathVariable("id") Long invoiceId) {
        return new ResponseEntity<>(invoiceService.replace(request, invoiceId), HttpStatus.OK);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long invoiceId) {
        invoiceService.delete(invoiceId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
