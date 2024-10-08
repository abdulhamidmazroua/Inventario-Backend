package com.hameed.inventario.service;

import com.hameed.inventario.model.dto.create.ProductCreateDTO;
import com.hameed.inventario.model.dto.update.ProductDTO;
import com.hameed.inventario.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    // Create a new product
    public ProductDTO addProduct(ProductCreateDTO productCreateDTO);

    // Update an existing product
    public ProductDTO updateProduct (ProductDTO productDTO);

    // Remove a product
    public void removeProduct(Long productId);

    // Get all products with pagination
    public Page<ProductDTO> getAllProducts(Pageable pageable);

    // Get a product by ID
    public ProductDTO getProductById(Long productId);

    public Product getProductEntityById(Long productId);
}

