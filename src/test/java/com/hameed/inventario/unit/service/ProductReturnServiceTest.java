package com.hameed.inventario.unit.service;


import com.hameed.inventario.exception.ResourceNotFoundException;
import com.hameed.inventario.mapper.ProductReturnMapper;
import com.hameed.inventario.model.dto.create.ProductReturnCreateDTO;
import com.hameed.inventario.model.dto.update.*;
import com.hameed.inventario.model.entity.*;
import com.hameed.inventario.repository.ProductReturnRepository;
import com.hameed.inventario.service.impl.CustomerServiceImpl;
import com.hameed.inventario.service.impl.InventoryStockServiceImpl;
import com.hameed.inventario.service.impl.ProductReturnServiceImpl;
import com.hameed.inventario.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductReturnServiceTest {

    @Mock
    private ProductReturnRepository productReturnRepository;
    @Mock
    private ProductReturnMapper productReturnMapper;
    @Mock
    private CustomerServiceImpl customerService;
    @Mock
    private ProductServiceImpl productService;
    @Mock
    private InventoryStockServiceImpl inventoryStockService;

    @InjectMocks
    private ProductReturnServiceImpl productReturnService;



    @Test
    @DisplayName("ProductReturn Added Successfully")
    public void testAddProductReturn_shouldReturnAddedProductReturnAsProductReturnDTO() {
        //  --- Arrange ---
        Customer customer = new Customer();
        Product product = new Product();
        ProductReturnCreateDTO productReturnCreateDTO = ProductReturnCreateDTO.builder()
                .customerId(1L)
                .productId(1L)
                .quantityReturned(2)
                .reason("Not needed anymore")
                .build();
        ProductReturn mockMappedProductReturn = ProductReturn.builder()
                .quantityReturned(2)
                .reason("Not needed anymore")
                .build();
        ProductReturn mockResultProductReturn = ProductReturn.builder()
                .customer(customer)
                .product(product)
                .quantityReturned(2)
                .reason("Not needed anymore")
                .build();
        mockResultProductReturn.setId(1L);
        ProductReturnDTO expectedProductReturnDTO = ProductReturnDTO.builder()
                .id(1L)
                .customer(new CustomerDTO())
                .product(new ProductDTO())
                .quantityReturned(2)
                .reason("Not needed anymore")
                .build();
        
        // Define behavior of the mocks
        Mockito.when(productReturnMapper.productReturnCreateDTOToProductReturn(productReturnCreateDTO)).thenReturn(mockMappedProductReturn);
        Mockito.when(customerService.getCustomerEntityById(productReturnCreateDTO.getCustomerId())).thenReturn(customer);
        Mockito.when(productService.getProductEntityById(productReturnCreateDTO.getProductId())).thenReturn(product);
        Mockito.when(productReturnRepository.save(mockMappedProductReturn)).thenReturn(mockResultProductReturn);
        Mockito.when(productReturnMapper.productReturnToProductReturnDTO(mockResultProductReturn)).thenReturn(expectedProductReturnDTO);

        // --- Act ---
        ProductReturnDTO resultProductReturnDTO = productReturnService.addProductReturn(productReturnCreateDTO);

        // --- Assert ---
        assertAll(
                () -> assertNotNull(resultProductReturnDTO, "result productReturn DTO is null"),
                () -> assertNotNull(resultProductReturnDTO.getId(), "result productReturn DTO does not include an ID"),
                () -> assertEquals(expectedProductReturnDTO, resultProductReturnDTO, "Expected productReturn DTO is not correct") // we can use this because lombok automatically generates a hasCode and equals methods for us
        );
    }

    @Test
    @DisplayName("ProductReturn retrieved successfully")
    public void testGetProductReturn_shouldReturnProductReturnAsProductReturnDTO() {
        //  --- Arrange ---
        Long productReturnId = 1L;
        ProductReturn mockResultProductReturn = ProductReturn.builder()
                .customer(new Customer())
                .product(new Product())
                .quantityReturned(2)
                .reason("Not needed anymore")
                .build();
        mockResultProductReturn.setId(1L);
        ProductReturnDTO expectedProductReturnDTO = ProductReturnDTO.builder()
                .id(1L)
                .customer(new CustomerDTO())
                .product(new ProductDTO())
                .quantityReturned(2)
                .reason("Not needed anymore")
                .build();
        // Define behavior of the mocks
        Mockito.when(productReturnRepository.findById(productReturnId)).thenReturn(Optional.of(mockResultProductReturn));
        Mockito.when(productReturnMapper.productReturnToProductReturnDTO(mockResultProductReturn)).thenReturn(expectedProductReturnDTO);

        // --- Act ---
        ProductReturnDTO resultProductReturnDTO = productReturnService.getProductReturnById(productReturnId);

        // --- Assert ---
        assertAll(
                () -> assertNotNull(resultProductReturnDTO, "result productReturn DTO is null"),
                () -> assertNotNull(resultProductReturnDTO.getId(), "result productReturn DTO does not include an ID"),
                () -> assertEquals(expectedProductReturnDTO, resultProductReturnDTO, "Expected productReturn DTO is not correct") // we can use this because lombok automatically generates a hasCode and equals methods for us
        );
    }

    @Test
    @DisplayName("ProductReturn successfully not retrieved because productReturn was not found")
    public void testGetProductReturn_shouldThrowResourceNotFoundException() {
        // --- Arrange ---
        Long productReturnId = 1L;


        // Define behavior of the mocks
        Mockito.when(productReturnRepository.findById(productReturnId)).thenReturn(Optional.empty());

        // --- Act and Assert ---
        assertThrows(ResourceNotFoundException.class, () -> productReturnService.getProductReturnById(productReturnId), "Expected ResourceNotFoundException to be thrown");
    }

}