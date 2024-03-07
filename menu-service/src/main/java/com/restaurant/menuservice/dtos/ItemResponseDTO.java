package com.restaurant.menuservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restaurant.menuservice.enums.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemResponseDTO {

    private String itemId;
    private String itemName;
    private String itemDetails;
    @Schema(allowableValues = "VEG, NON_VEG")
    private String itemType;
    private CategoryDTO category;
    private PriceDTO price;
    private String imageUrl;

    @JsonIgnore
    public ItemType getItemTypeEnum() {
        return ItemType.fromString(itemType);
    }
}
