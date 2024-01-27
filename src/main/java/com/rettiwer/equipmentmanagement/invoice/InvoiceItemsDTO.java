package com.rettiwer.equipmentmanagement.invoice;

import com.fasterxml.jackson.annotation.*;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class InvoiceItemsDTO {
        private Long id;
        @NonNull
        @NotEmpty
        @Size(min = 4)
        private String invoiceId;
        @NonNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate invoiceDate;

        @NonNull
        private List<ItemDTO> items;
}
