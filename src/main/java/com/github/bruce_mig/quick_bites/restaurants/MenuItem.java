package com.github.bruce_mig.quick_bites.restaurants;

import java.math.BigDecimal;

public record MenuItem(
        String id,
        String restaurantId,
        String name,
        String description,
        BigDecimal price,
        String category,
        boolean available
) {}