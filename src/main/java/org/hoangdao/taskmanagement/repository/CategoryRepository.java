package org.hoangdao.taskmanagement.repository;

import org.hoangdao.taskmanagement.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByName(String name);
}
