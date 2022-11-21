package ee.taltech.iti0302.robotiklubi.mappers.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.repository.WikiTag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WikiTagMapper {

    TagDto toDto(WikiTag wikiTag);

    List<TagDto> toDtoList(List<WikiTag> wikiTags);
}
