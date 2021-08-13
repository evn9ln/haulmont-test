package com.haulmonttest.repo;

import com.haulmonttest.domain.UuidMap;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface UuidMapRepository extends JpaRepository<UuidMap, Integer> {

    Optional<UuidMap> findById(Integer id);

    UuidMap findByUuid(UUID uuid);

    @Transactional
    void deleteByUuid(UUID uuid);
}
