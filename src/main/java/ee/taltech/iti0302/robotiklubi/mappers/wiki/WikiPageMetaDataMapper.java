package ee.taltech.iti0302.robotiklubi.mappers.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageMetaDataDto;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WikiPageMetaDataMapper {

    WikiPageMetaDataDto toDto(WikiPage wikiPage);

    List<WikiPageMetaDataDto> toDtoList(List<WikiPage> wikiPages);
}
