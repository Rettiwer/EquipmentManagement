package com.rettiwer.equipmentmanagement.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 32)
    private String name;

    @NotNull
    private BigDecimal price;

    private String comment;

    @NotNull
    private Integer ownerId;

    @NotNull
    private Long invoiceId;
}
