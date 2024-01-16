package com.rettiwer.equipmentmanagement.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rettiwer.equipmentmanagement.invoice.Invoice;
import com.rettiwer.equipmentmanagement.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;


}
