package com.codexdrive.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private String cartId;
    private Date createdAt;
    private UserDto user;
    //mapping cart-items ;
    private List<CartItemDto> items = new ArrayList<>();

    private int cartItemId;

    private ProductDto product;

    private int quantity;

    private int totalPrice;

}