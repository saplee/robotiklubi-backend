package ee.taltech.iti0302.robotiklubi.dto.wiki;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class WikiPageDto {

    private Long id;
    private String title;
    private String content;
    private Integer authorId;
    private String authorName;
    private Integer lastEditorId;
    private String lastEditorName;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastEdited;
}
