package com.rettiwer.equipmentmanagement.invoice;

import com.rettiwer.equipmentmanagement.apierror.exception.InsufficientPermissionException;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.item.Item;
import com.rettiwer.equipmentmanagement.user.User;
import com.rettiwer.equipmentmanagement.user.UserRepository;
import com.rettiwer.equipmentmanagement.user.role.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;
    private final AuthenticationService authService;

    public InvoiceItemsDTO getById(Long invoiceId) {
        User currentUser = authService.getCurrentUser();

        if (currentUser.hasAnyRole(List.of(Role.UserRole.ROLE_ADMIN, Role.UserRole.ROLE_SUPERVISOR)))
            throw new InsufficientPermissionException();

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(EntityNotFoundException::new);

        if (currentUser.hasRole(Role.UserRole.ROLE_ADMIN))
            return invoiceMapper.toDtoWithItems(invoice);

        List<Item> items = invoice.getItems().stream().filter(item ->
                        item.getOwner().getId().equals(currentUser.getId()) ||
                                (item.getOwner().getSupervisor() != null && item.getOwner().getSupervisor().getId().equals(currentUser.getId())))
                .toList();

        if (items.isEmpty())
            throw new InsufficientPermissionException();

        invoice.setItems(items);

        return invoiceMapper.toDtoWithItems(invoice);
    }

    @Transactional
    public InvoiceItemsDTO create(InvoiceItemsDTO invoiceItemsDTO) {
        User currentUser = authService.getCurrentUser();

        if (currentUser.hasAnyRole(List.of(Role.UserRole.ROLE_ADMIN, Role.UserRole.ROLE_SUPERVISOR)))
            throw new InsufficientPermissionException();

        invoiceItemsDTO.setId(null);

        Invoice invoice = invoiceMapper.toEntityWithItems(invoiceItemsDTO);

        invoice.getItems().forEach(item -> item.setInvoice(invoice));

        return invoiceMapper.toDtoWithItems(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceItemsDTO replace(InvoiceItemsDTO invoiceItemsDTO, Long invoiceId) {
        User currentUser = authService.getCurrentUser();

        if (currentUser.hasAnyRole(List.of(Role.UserRole.ROLE_ADMIN, Role.UserRole.ROLE_SUPERVISOR)))
            throw new InsufficientPermissionException();

        Invoice invoice = invoiceRepository.findById(invoiceItemsDTO.getId()).orElseThrow(EntityNotFoundException::new);

        Invoice toUpdateInvoice = invoiceMapper.updateEntityWithItems(invoiceItemsDTO, invoice);

        toUpdateInvoice.getItems().forEach(item -> item.setInvoice(toUpdateInvoice));

        return invoiceMapper.toDtoWithItems(invoiceRepository.save(toUpdateInvoice));
    }

    public void delete(Long invoiceId) {
        User currentUser = authService.getCurrentUser();

        if (currentUser.hasAnyRole(List.of(Role.UserRole.ROLE_ADMIN, Role.UserRole.ROLE_SUPERVISOR)))
            throw new InsufficientPermissionException();

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(EntityNotFoundException::new);

        //Check when user is supervisor and any of items not belong to him or his employees
        if (!currentUser.hasRole(Role.UserRole.ROLE_ADMIN) &&  invoice.getItems().stream().noneMatch(item ->
                Objects.equals(item.getOwner().getId(), currentUser.getId()) ||
                        currentUser.getEmployees().stream().anyMatch(user -> Objects.equals(user.getId(), item.getOwner().getId()))
        ))
            throw new InsufficientPermissionException();

        invoiceRepository.delete(invoice);
    }
}
