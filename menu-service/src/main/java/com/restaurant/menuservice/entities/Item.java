package com.restaurant.menuservice.entities;

import com.restaurant.menuservice.enums.ItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Item", indexes = {
        @Index(name = "idx_itemName_itemType_categoryId", columnList = "itemName, itemType, category_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    private String itemId;

    @NotBlank
    @NotNull
    @Column(unique = true)
    private String itemName;

    @NotNull
    @NotBlank
    private String itemDetails;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ItemType itemType;

    @NotNull
    private Double defaultPrice;

    @Column(columnDefinition = "json")
    private String discountDetails;

    @Column
    private String imageUrl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


}
