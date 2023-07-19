package org.hoangdao.taskmanagement.repository;

import org.hoangdao.taskmanagement.entity.Magazine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MagazineRepository extends MongoRepository<Magazine, String> {
}
