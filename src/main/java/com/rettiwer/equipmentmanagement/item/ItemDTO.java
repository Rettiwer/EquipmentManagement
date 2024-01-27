package com.rettiwer.equipmentmanagement.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rettiwer.equipmentmanagement.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO {
    private Long id;

    @NonNull
    @NotEmpty
    @Size(min = 2, max = 32)
    private String name;

    @NonNull
    @NotNull
    private BigDecimal price;

    @NonNull
    private String comment;

    @NonNull
    @NotNull
    private Integer ownerId;

    private Long invoiceId;
}
