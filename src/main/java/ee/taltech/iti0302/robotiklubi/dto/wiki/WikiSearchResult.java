package ee.taltech.iti0302.robotiklubi.dto.wiki;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class WikiSearchResult {

    private Long totalResults;
    private Integer prevPage;
    private Integer nextPage;
    private List<WikiPageMetaDataDto> results;
    private Long timeTaken;
}
