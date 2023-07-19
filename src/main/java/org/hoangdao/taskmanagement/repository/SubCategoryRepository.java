package org.hoangdao.taskmanagement.repository;

import org.hoangdao.taskmanagement.entity.Magazine;
import org.hoangdao.taskmanagement.entity.SubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubCategoryRepository extends MongoRepository<SubCategory, String> {
    boolean existsByNameAndCategoryId(String name, String categoryId);
}
