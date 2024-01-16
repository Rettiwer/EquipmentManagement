package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.item.Item;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String invoice_id;

    private Date invoice_date;

    @OneToMany(mappedBy = "invoice")
    private List<Item> items;
}
