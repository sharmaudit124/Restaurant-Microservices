package com.restaurant.menuservice.enums;

public enum ItemType {
    VEG,
    NON_VEG;

    public static ItemType fromString(String value) {
        try {
            return ItemType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid item type: " + value);
        }
    }
}
