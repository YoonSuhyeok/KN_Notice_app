package com.deadline.knunotice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticesRepository extends JpaRepository<Notices, Long> {
}