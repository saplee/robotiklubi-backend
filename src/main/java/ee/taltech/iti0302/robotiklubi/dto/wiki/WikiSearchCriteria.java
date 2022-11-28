package ee.taltech.iti0302.robotiklubi.dto.wiki;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WikiSearchCriteria {

    private String titleSearch;
    private String contentSearch;
    private boolean sortAscending;
    private boolean sortByTitle;
    private boolean sortByCreationDate;
    private boolean sortByEditDate;
    private List<Integer> tags;
    private Integer resultsPerPage;
    private Integer firstResult;
}
