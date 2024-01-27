package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.item.Item;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody InvoiceItemsDTO request) {
        return new ResponseEntity<>(invoiceService.createWithItems(request), HttpStatus.CREATED);
    }
}
