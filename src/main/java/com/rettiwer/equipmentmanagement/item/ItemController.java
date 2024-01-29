package com.rettiwer.equipmentmanagement.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ItemDTO itemDTO) {
        return new ResponseEntity<>(itemService.insert(itemDTO), HttpStatus.CREATED);
    }

    @GetMapping(value =  "/{id}")
    public ResponseEntity<?> single(@PathVariable Long id) {
        return new ResponseEntity<>(itemService.getItemById(id), HttpStatus.OK);
    }

    @PutMapping(value =  "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ItemDTO itemDTO, @Nullable @PathVariable("id") Item item) {
        return new ResponseEntity<>(itemService.replaceOrInsert(itemDTO, item),
                item != null ? HttpStatus.OK : HttpStatus.CREATED);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<?> delete(@Nullable @PathVariable("id") Item item) {
        return new ResponseEntity<>(itemService.delete(item));
    }
}
