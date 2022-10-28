package ee.taltech.iti0302.robotiklubi.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@Table(name = "wiki")
@Entity
public class WikiPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "author")
    private Integer authorId;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "last_edited")
    private OffsetDateTime lastEdited;

    @PrePersist
    public void saveCreationTime() {
        createdAt = OffsetDateTime.now();
        lastEdited = OffsetDateTime.now();
    }

    @PreUpdate
    public void saveEditedTime() {
        lastEdited = OffsetDateTime.now();
    }
}
