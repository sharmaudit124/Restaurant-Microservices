package com.restaurant.menuservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Category", indexes = {
        @Index(name = "idx_categoryName", columnList = "categoryName")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    private String categoryId;
    @NotBlank
    @Column(unique = true)
    private String categoryName;
}
