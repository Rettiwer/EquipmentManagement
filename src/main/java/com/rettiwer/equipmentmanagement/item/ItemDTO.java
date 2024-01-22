package com.rettiwer.equipmentmanagement.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String name;
    private BigDecimal price;
    private String comment;
    private Integer ownerId;
    @JsonIgnore
    private Long invoiceId;
}
