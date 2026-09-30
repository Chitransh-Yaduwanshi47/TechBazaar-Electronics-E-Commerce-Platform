package com.codexdrive.electronic.store.repositories;

import com.codexdrive.electronic.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category , String> {


}