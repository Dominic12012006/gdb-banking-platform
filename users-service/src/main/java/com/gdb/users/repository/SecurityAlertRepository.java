package com.gdb.users.repository;

import com.gdb.users.domain.model.SecurityAlertLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SecurityAlertRepository extends JpaRepository<SecurityAlertLog, Long> {

    List<SecurityAlertLog> findAllByOrderByAlertDateDesc();

    List<SecurityAlertLog> findByUserId(Long userId);

    List<SecurityAlertLog> findByAlertDateBetweenOrderByAlertDateDesc(
            LocalDateTime from, LocalDateTime to);
}