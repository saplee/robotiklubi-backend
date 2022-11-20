package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageMetaDataDto;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMapper;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMetaDataMapper;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiTagMapper;
import ee.taltech.iti0302.robotiklubi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class WikiService {

    private final WikiRepository wikiRepository;
    private final WikiPageMapper wikiPageMapper;
    private final WikiPageMetaDataMapper wikiPageMetaDataMapper;
    private final WikiTagRepository wikiTagRepository;
    private final WikiTagMapper wikiTagMapper;
    private final WikiTagRelationRepository wikiTagRelationRepository;

    public WikiPageDto getPageById(Long id) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(id);
        if (pageOptional.isPresent()) {
            WikiPage page = pageOptional.get();
            page.setTitle(page.getTitle().strip());
            return wikiPageMapper.toDto(pageOptional.get());
        }
        WikiPageDto dto = new WikiPageDto();
        dto.setTitle("");
        dto.setContent("");
        return dto;
    }

    public List<TagDto> getPageTags(Long id) {
        List<Long> tagIds = wikiTagRelationRepository.findAllByPageId(id.intValue()).stream().map(r -> Long.valueOf(r.getTagId())).toList();
        List<WikiTag> tags = wikiTagRepository.findAllByIdIn(tagIds);
        return wikiTagMapper.toDtoList(tags);
    }

    public List<WikiPageMetaDataDto> getPagesByTag(Long id) {
        List<Long> pageIds = wikiTagRelationRepository.findAllByTagId(id.intValue()).stream().map(r -> Long.valueOf(r.getPageId())).toList();
        List<WikiPage> pages = wikiRepository.findAllById(pageIds);
        return wikiPageMetaDataMapper.toDtoList(pages);
    }
}
