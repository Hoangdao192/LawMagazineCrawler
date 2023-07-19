package org.hoangdao.taskmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
public class SubCategory {

    private String id;
    private String name;
    private String categoryId;

}
