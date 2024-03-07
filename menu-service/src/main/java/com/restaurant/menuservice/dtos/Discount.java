package com.restaurant.menuservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    private LocalDate validFrom;
    private LocalDate validTo;
    private Double discountPercentage;
    private Double discountedPrice;
}
