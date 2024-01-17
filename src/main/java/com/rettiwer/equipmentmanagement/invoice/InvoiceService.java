package com.rettiwer.equipmentmanagement.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository repository) {
        this.invoiceRepository = repository;
    }

    public Invoice create(InvoiceDTO invoiceDTO) {
        Invoice invoice = convertToEntity(invoiceDTO);
        return invoiceRepository.save(invoice);
    }

    private InvoiceDTO convertToDto(Invoice invoice) {
        return new InvoiceDTO(invoice.getId(),
                invoice.getInvoiceId(),
                invoice.getInvoiceDate(),
                invoice.getItems());
    }

    private Invoice convertToEntity(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();

        invoice.setId(invoiceDTO.id());
        invoice.setInvoiceId(invoiceDTO.invoice_id());
        invoice.setInvoiceDate(invoiceDTO.invoice_date());
        invoice.setItems(invoiceDTO.items());


        return invoice;
    }

}
