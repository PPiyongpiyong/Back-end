package com.example.springserver.api.Manual.Repository;

import com.example.springserver.api.Manual.Domain.Manual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ManualCategoryRepository extends JpaRepository<Manual, Long> {
    List<Manual> findByCategory(String category);  // 카테고리로 매뉴얼 찾기
}
