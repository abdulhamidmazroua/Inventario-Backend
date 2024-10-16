package com.hameed.inventario.model.dto.update;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PurchaseDTO {
    private Long id;

    private String purchaseNumber;

    private String purchaseStatus;

    private Double discount;

    private Double totalAmount;

    private String notes;

    private SupplierDTO supplier;

    private List<POLineDTO> purchaseLines; // change this to purchaseLines
}
