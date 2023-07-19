package org.hoangdao.taskmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Magazine {

    @Id
    private String id;
    private String title;
    private String shortDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MagazineContent.AbstractContent> contents;
    private String url;
    private String thumbnail;

}
