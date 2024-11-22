package com.collect.data.repository;

import com.collect.data.entity.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity, Long> {
    ComplaintEntity findByEmail(String email);
}
