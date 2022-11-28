package ee.taltech.iti0302.robotiklubi.dto.wiki;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class WikiPageMetaDataDto {

    private Long id;
    private String title;
    private String summary;
    private Integer author;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastEdited;
}
