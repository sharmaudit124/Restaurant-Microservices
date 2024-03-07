package com.restaurant.menuservice.repositories;

import com.restaurant.menuservice.entities.Item;
import com.restaurant.menuservice.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    Optional<Item> findByItemNameIgnoreCase(String itemName);

    List<Item> findByCategory_CategoryName(String categoryName);

    List<Item> findByItemType_(ItemType itemType);

    List<Item> findByCategory_CategoryNameAndItemType(String categoryName, ItemType itemType);

    List<Item> findByDiscountDetailsContaining(String discountDetailsJson);

    List<Item> findByDefaultPrice(Double itemPrice);

    @Query("SELECT i FROM Item i WHERE " +
            "LOWER(i.itemName) LIKE %:keyword% OR " +
            "LOWER(i.itemDetails) LIKE %:keyword% OR " +
            "LOWER(i.itemType) LIKE %:keyword% OR " +
            "CAST(i.defaultPrice AS string) LIKE %:keyword% OR " +
            "i.discountDetails LIKE %:keyword%")
    List<Item> getItemBySearchKeyword(@Param("keyword") String keyword);
}
