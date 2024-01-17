package com.rettiwer.equipmentmanagement.invoice;


import com.rettiwer.equipmentmanagement.item.Item;

import java.util.Date;
import java.util.List;

public record InvoiceDTO(
        Long id,
        String invoice_id,
        Date invoice_date,
        List<Item> items
) {
}
