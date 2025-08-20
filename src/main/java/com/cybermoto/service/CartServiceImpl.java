package com.cybermoto.service;

import com.cybermoto.entity.Cart;
import com.cybermoto.entity.CartItem;
import com.cybermoto.entity.Client;
import com.cybermoto.entity.Product;
import com.cybermoto.repository.CartItemRepository;
import com.cybermoto.repository.CartRepository;
import com.cybermoto.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;

  @Override
  public Cart getOrCreateCart(Client client) {
      return cartRepository.findByClientId(client.getId())
              .orElseGet(() -> {
                  Cart c = new Cart();
                  c.setClient(client);
                  return cartRepository.save(c);
              });
  }

  @Override
  @Transactional
  public Cart addItem(Client client, Long productId, int quantity) {
      if(quantity <= 0){ quantity = 1;}
      Cart cart = getOrCreateCart(client);
      Product product = productRepository.findById(productId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

      Optional<CartItem> existing = cart.getItems().stream()
              .filter(ci -> ci.getProduct().getId().equals(productId)).findFirst();

      if(existing.isPresent()) {
          CartItem ci = existing.get();
          ci.setQuantity(ci.getQuantity() + quantity);
          ci.setUnitPrice(product.getPrice());
          ci.setTotalPrice(ci.getTotalPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
          cartItemRepository.save(ci);
      }
      else {
          CartItem ci = new CartItem();
          ci.setCart(cart);
          ci.setProduct(product);
          ci.setProductNameSnapshot(product.getProductName());
          ci.setProductBrandSnapshot(product.getBrand());
          ci.setMainImageFilename(product.getMainImage() != null ? product.getMainImage().getFilename() : null);
          ci.setQuantity(quantity);
          ci.setUnitPrice(product.getPrice());
          ci.setTotalPrice(ci.getTotalPrice().multiply(BigDecimal.valueOf(quantity)));
          cart.getItems().add(ci);
      }

      recalc(cart);
      return cartRepository.save(cart);
  }

  @Override
  @Transactional
  public Cart updateQuantity(Client client, Long itemId, int quantity) {
      if(quantity <= 0){quantity = 1;}

      Cart cart = getOrCreateCart(client);
      CartItem item = cartItemRepository.findById(itemId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      if(!item.getProduct().getId().equals(cart.getId())) {
          throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }

      item.setQuantity(quantity);
      item.setTotalPrice(item.getTotalPrice().multiply(BigDecimal.valueOf(quantity)));
      cartItemRepository.save(item);

      recalc(cart);
      return cartRepository.save(cart);
  }

  @Override
  @Transactional
  public Cart removeItem(Client client, Long itemId) {
      Cart cart = getOrCreateCart(client);
      CartItem item = cartItemRepository.findById(itemId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

      if(!item.getCart().getId().equals(cart.getId())) {
          throw new ResponseStatusException(HttpStatus.FORBIDDEN);
      }

      cart.getItems().remove(item);
      cartItemRepository.delete(item);

      recalc(cart);
      return cartRepository.save(cart);
  }

  //esvazia o carrinho
  @Override
  @Transactional
  public Cart clear(Client client) {
      Cart cart = getOrCreateCart(client);
      cart.getItems().clear();
      recalc(cart);
      return cartRepository.save(cart);
  }

  @Override
  public Cart getCart(Client client) {
      return getOrCreateCart(client);
  }

  public void recalc(Cart cart) {
      BigDecimal subtotal = BigDecimal.ZERO;

      for(CartItem it : cart.getItems()) {
          subtotal = subtotal.add(it.getTotalPrice());
      }

      cart.setSubtotal(subtotal);
      cart.setFreight(BigDecimal.ZERO);
      cart.setDiscount(BigDecimal.ZERO);
      cart.setTotal(subtotal.add(cart.getFreight()).subtract(cart.getDiscount()));
  }
}
