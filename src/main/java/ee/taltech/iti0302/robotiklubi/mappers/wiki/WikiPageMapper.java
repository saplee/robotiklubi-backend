package ee.taltech.iti0302.robotiklubi.mappers.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WikiPageMapper {

    @Mapping(expression = "java(wikiPage.getAuthorFullName())", target = "authorName")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(expression = "java(wikiPage.getLastEditorFullName())", target = "lastEditorName")
    @Mapping(source = "lastEditedBy.id", target = "lastEditorId")
    WikiPageDto toDto(WikiPage wikiPage);
}
