package ee.taltech.iti0302.robotiklubi.repository;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "author")
    private User author;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "last_edited")
    private OffsetDateTime lastEdited;
    @ManyToOne
    @JoinColumn(name = "last_edited_by")
    private User lastEditedBy;

    @PrePersist
    public void saveCreationTime() {
        createdAt = OffsetDateTime.now();
        lastEdited = OffsetDateTime.now();
    }

    @PreUpdate
    public void saveEditedTime() {
        lastEdited = OffsetDateTime.now();
    }

    public String getSummary() {
        return content.substring(0, Math.min(400, content.length())) + "...";
    }

    public String getAuthorFullName() {
        if (author == null) return null;
        return author.getFullName();
    }

    public String getLastEditorFullName() {
        if (lastEditedBy == null) return null;
        return lastEditedBy.getFullName();
    }
}
