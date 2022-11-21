package ee.taltech.iti0302.robotiklubi.dto.wiki;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class WikiPageMetaDataDto {

    private Long id;
    private String title;
    private Integer author;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastEdited;
}
