package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.CycleAvoidingMappingContext;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {

    InvoiceDTO toDto(Invoice invoice);

    Invoice toEntity(InvoiceDTO invoiceDTO);

    InvoiceItemsDTO toDtoWithItems(Invoice invoice, @Context CycleAvoidingMappingContext context);


    Invoice toEntityWithItems(InvoiceItemsDTO invoiceItemsDTO, @Context CycleAvoidingMappingContext context);

    List<InvoiceItemsDTO> toListDtoWithItems(List<Invoice> invoiceList, @Context CycleAvoidingMappingContext context);
}
