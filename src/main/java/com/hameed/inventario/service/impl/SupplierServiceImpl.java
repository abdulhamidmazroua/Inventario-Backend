package com.hameed.inventario.service.impl;

import com.hameed.inventario.exception.ResourceNotFoundException;
import com.hameed.inventario.mapper.SupplierMapper;
import com.hameed.inventario.model.dto.update.SupplierDTO;
import com.hameed.inventario.model.entity.Supplier;
import com.hameed.inventario.repository.SupplierRepository;
import com.hameed.inventario.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public SupplierDTO addSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = SupplierMapper.INSTANCE.supplierDTOToSupplier(supplierDTO);
        supplierRepository.save(supplier);
        return SupplierMapper.INSTANCE.supplierToSupplierDTO(supplier);
    }

    @Override
    public SupplierDTO updateSupplier(SupplierDTO supplierDTO) {
        Long supplierId = supplierDTO.getId();
        supplierRepository.findById(supplierId).ifPresentOrElse(
                supplier -> {
                    // map fields of dto to supplier
                    supplier.setSupplierName(supplierDTO.getSupplierName());
                    supplier.setContactName(supplierDTO.getContactName());
                    supplier.setContactPhone(supplierDTO.getContactPhone());
                    supplier.setEmail(supplierDTO.getEmail());
                    supplier.setAddress(supplierDTO.getAddress());

                    // save
                    supplierRepository.save(supplier);
                },
                () -> {
                    throw new ResourceNotFoundException("Supplier with this Id: " + supplierId + " could not be found");
                }
        );
        Optional<Supplier> optionalSupplier = supplierRepository.findById(supplierId);
        if(optionalSupplier.isPresent()) {
            Supplier supplier = optionalSupplier.get();
            // map fields of dto to supplier
            supplier.setSupplierName(supplierDTO.getSupplierName());
            supplier.setContactName(supplierDTO.getContactName());
            supplier.setContactPhone(supplierDTO.getContactPhone());
            supplier.setEmail(supplierDTO.getEmail());
            supplier.setAddress(supplierDTO.getAddress());

            // save
            supplierRepository.save(supplier);

            // return the updated DTO
            return SupplierMapper.INSTANCE.supplierToSupplierDTO(supplier);
        } else {
            throw new ResourceNotFoundException("Supplier with this Id: " + supplierId + " could not be found");
        }
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        supplierRepository.findById(supplierId).ifPresentOrElse(
                supplier -> {
                    // handling the link with other entities before deleting
                    supplier.getPurchaseOrders().forEach(purchaseOrder -> purchaseOrder.setSupplier(null));
                    supplier.getProducts().forEach(product -> product.removeSupplier(supplier));

                    supplierRepository.delete(supplier);
                },
                () -> {
                    throw new ResourceNotFoundException("Supplier with this Id: " + supplierId + " could not be found");
                }
        );
    }

    @Override
    public SupplierDTO getSupplierById(Long supplierId) {
        Supplier supplier = getSupplierEntityById(supplierId);
        return SupplierMapper.INSTANCE.supplierToSupplierDTO(supplier);
    }

    @Override
    public Page<SupplierDTO> getAllSuppliers(Pageable pageable) {
        Page<Supplier> pageSuppliers = supplierRepository.findAll(pageable);
        return pageSuppliers.map(SupplierMapper.INSTANCE::supplierToSupplierDTO);
    }

    @Override
    public Supplier getSupplierEntityById(Long supplierId) {
        return supplierRepository.findById(supplierId).orElseThrow(() -> new ResourceNotFoundException("Supplier with this Id: " + supplierId + " could not be found"));
    }
}
