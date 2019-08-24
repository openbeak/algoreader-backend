package com.jyami.algoreaderbackend.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BaekJoonRepositroy extends MongoRepository<BaekJoon, Long> {
    List<BaekJoon> findByNumber(long number);
    List<BaekJoon> findByNumberIn(Collection<Long> number);
}
