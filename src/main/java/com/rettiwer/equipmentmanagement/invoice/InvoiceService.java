package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.item.UserItemsDTO;
import com.rettiwer.equipmentmanagement.user.UserRepository;
import com.rettiwer.equipmentmanagement.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;
    private final AuthenticationService authService;

    public List<UserItemsDTO> getAll() {
        User user = authService.getCurrentUser();

        if (user.hasRole(Role.UserRole.ROLE_ADMIN))
            invoiceMapper.toListDtoWithItems(invoiceRepository.findAll());

        return null;
    }

    public List<InvoiceItemsDTO> getById(Integer invoiceId) {
        return invoiceMapper.toListDtoWithItems(invoiceRepository.findAll());
    }

    @Transactional
    public InvoiceItemsDTO create(InvoiceItemsDTO invoiceItemsDTO) {
        Invoice invoice = invoiceMapper.toEntityWithItems(invoiceItemsDTO);

        invoice.getItems().forEach(item -> item.setInvoice(invoice));

        return invoiceMapper.toDtoWithItems(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceItemsDTO replace(InvoiceItemsDTO invoiceItemsDTO, Integer invoiceId) {
        Invoice invoice = invoiceMapper.toEntityWithItems(invoiceItemsDTO);

        invoice.getItems().forEach(item -> item.setInvoice(invoice));

        return invoiceMapper.toDtoWithItems(invoiceRepository.save(invoice));
    }

    public void delete(Integer invoiceId) {

    }
}
