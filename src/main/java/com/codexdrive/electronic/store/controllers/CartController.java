package com.codexdrive.electronic.store.controllers;

import com.codexdrive.electronic.store.config.AppConstants;
import com.codexdrive.electronic.store.dtos.AddItemToCartRequest;
import com.codexdrive.electronic.store.dtos.ApiResponseMessage;
import com.codexdrive.electronic.store.dtos.CartDto;
import com.codexdrive.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // add items to cart
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_ADMIN +"', '"+AppConstants.ROLE_NORMAL+"')")
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request) {
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    // clear cart
    @PreAuthorize("hasAnyRole('"+ AppConstants.ROLE_ADMIN +"', '"+AppConstants.ROLE_NORMAL+"')")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int itemID) {
        cartService.removeItemFromCart(userId, itemID);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Item is removed !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //create cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId, @PathVariable int itemID) {
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Now cart is blank !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get items from cart
    @PreAuthorize("hasAnyRole('"+AppConstants.ROLE_ADMIN+"', '"+AppConstants.ROLE_NORMAL+"')")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}