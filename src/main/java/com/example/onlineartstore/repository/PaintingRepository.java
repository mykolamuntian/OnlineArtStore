package com.example.onlineartstore.repository;

import com.example.onlineartstore.entity.Painting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaintingRepository extends JpaRepository<Painting, Integer> {
}
