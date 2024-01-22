package com.rettiwer.equipmentmanagement.item;

import com.fasterxml.jackson.annotation.*;
import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemInvoiceDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String comment;
   // private Integer ownerId;
    @JsonIgnoreProperties(value = { "items" })
    private InvoiceItemsDTO invoice;

}
