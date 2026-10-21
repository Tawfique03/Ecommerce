package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kodewala.ecommerce.model.CartItem;

public class CartRepository {
    private Map<Integer, List<CartItem>> carts = new HashMap<>();

    public List<CartItem> getCart(int customerId) {
        if (!carts.containsKey(customerId)) carts.put(customerId, new ArrayList<>());
        return carts.get(customerId);
    }
    public void clearCart(int customerId) { getCart(customerId).clear(); }
}
