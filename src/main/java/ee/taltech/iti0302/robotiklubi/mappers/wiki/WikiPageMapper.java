package ee.taltech.iti0302.robotiklubi.mappers.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WikiPageMapper {

    WikiPageDto toDto(WikiPage wikiPage);
}
