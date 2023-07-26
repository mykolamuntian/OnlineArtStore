package com.example.onlineartstore.repository;

import com.example.onlineartstore.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
