package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(itemService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> create(@RequestBody ItemDTO itemDTO) {
        return new ResponseEntity<>(itemService.add(itemDTO), HttpStatus.OK);
    }

    @GetMapping(value =  "/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }

    @PatchMapping(value =  "/{id}")
    public ResponseEntity<?> update(@RequestBody ItemDTO itemDTO, @PathVariable Long id) {
        return new ResponseEntity<>(itemService.replace(itemDTO, id), HttpStatus.OK);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
