package com.cybermoto.service;

import com.cybermoto.entity.Cart;
import com.cybermoto.entity.Client;
import jakarta.transaction.Transactional;

public interface CartService {

    Cart getOrCreateCart(Client client);

    Cart addItem(Client client, Long productId, int quantity);

    Cart updateQuantity(Client client, Long itemId, int quantity);


    Cart removeItem(Client client, Long itemId);

    Cart clear(Client client);

    Cart getCart(Client client);
}
