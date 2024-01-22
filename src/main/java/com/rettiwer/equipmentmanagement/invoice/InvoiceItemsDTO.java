package com.rettiwer.equipmentmanagement.invoice;

import com.fasterxml.jackson.annotation.*;
import com.rettiwer.equipmentmanagement.item.ItemInvoiceDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemsDTO {
        private Long id;
        @NotEmpty
        @Size(min = 4)
        private String invoiceId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate invoiceDate;

        @JsonIgnoreProperties(value = { "invoice" })
        private List<ItemInvoiceDTO> items;
}
