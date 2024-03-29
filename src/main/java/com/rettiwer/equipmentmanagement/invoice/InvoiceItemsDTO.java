package com.rettiwer.equipmentmanagement.invoice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import jakarta.validation.Valid;
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
        private LocalDate date;

        @NonNull
        @Valid
        private List<ItemDTO> items;
}
