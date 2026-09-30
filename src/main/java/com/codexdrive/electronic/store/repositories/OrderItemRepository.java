package com.codexdrive.electronic.store.repositories;

import com.codexdrive.electronic.store.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem , Integer> {
}