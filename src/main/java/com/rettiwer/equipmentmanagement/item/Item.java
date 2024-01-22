package com.rettiwer.equipmentmanagement.item;

import com.fasterxml.jackson.annotation.*;
import com.rettiwer.equipmentmanagement.invoice.Invoice;
import com.rettiwer.equipmentmanagement.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private String comment;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;*/

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name="invoice_id", nullable = false)
    @JsonIgnore
    private Invoice invoice;
}
