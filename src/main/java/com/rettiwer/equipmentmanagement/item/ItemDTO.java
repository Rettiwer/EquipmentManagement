package com.rettiwer.equipmentmanagement.item;

import com.rettiwer.equipmentmanagement.invoice.InvoiceDTO;
import com.rettiwer.equipmentmanagement.user.User;

import java.math.BigDecimal;

public record ItemDTO(
        String name,
        BigDecimal price,
        String comment,
        User owner,
        InvoiceDTO invoice
) {
}
