package com.github.bruce_mig.quick_bites.restaurants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Random;

@Service
public class RestaurantService  {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final DataLoader dataLoader;
    private final Random random = new Random();

    public RestaurantService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

//    /**
//     * Get menu from partner restaurant API (simulates flaky external API).
//     *
//     * @Retryable configuration:
//     * - default: All Exceptions & 3 retries
//     * - maxRetries: 4 (1 initial + 3 retries)
//     * - includes: Only retry on RestaurantApiException
//     * - backoff: Start with 1 second, multiply by 2 (exponential backoff)
//     */
    @Retryable(
            maxRetries = 4,
            includes = RestaurantApiException.class,
            delay = 1000, // 1-second delay
            multiplier = 2 // double the delay for each retry attempt

    )
    public List<MenuItem> getMenuFromPartner(String restaurantId) {
        log.info("🍽️  Fetching menu from restaurant partner API for: {}", restaurantId);

        // Simulate flaky external API (40% failure rate)
        if (random.nextDouble() < 0.4) {
            log.warn("⚠️ Restaurant API failed! Will retry...");
            throw new RestaurantApiException("Partner restaurant API is temporarily unavailable");
        }

        // Simulate network delay
        simulateDelay(Duration.ofMillis(200));

        Restaurant restaurant = dataLoader.getRestaurant(restaurantId);
        if (restaurant == null) {
            throw new RestaurantApiException("Restaurant not found: " + restaurantId);
        }

        List<MenuItem> menu = restaurant.menuItemIds().stream()
                .map(dataLoader::getMenuItem)
                .filter(item -> item != null && item.available())
                .toList();

        log.info("✅ Successfully fetched {} menu items from {}", menu.size(), restaurant.name());
        return menu;
    }

    public List<Restaurant> findAll() {
        return dataLoader.getRestaurants().values().stream().toList();
    }


    private void simulateDelay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
