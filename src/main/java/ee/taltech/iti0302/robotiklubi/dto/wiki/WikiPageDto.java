package ee.taltech.iti0302.robotiklubi.dto.wiki;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class WikiPageDto {

    private Long id;
    private String title;
    private String content;
    private Integer author;
    private Integer lastEditedBy;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastEdited;
}
