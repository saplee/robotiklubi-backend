package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.TagListDto;
import ee.taltech.iti0302.robotiklubi.exception.NotFoundException;
import ee.taltech.iti0302.robotiklubi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class WikiTagRelationService {

    private final WikiTagRelationRepository relationRepository;
    private final WikiRepository wikiRepository;
    private final WikiTagRepository tagRepository;

    public void createRelation(Long pageId, Long tagId) {
        Optional<WikiPage> optionalPage = wikiRepository.findById(pageId);
        Optional<WikiTag> optionalTag = tagRepository.findById(tagId);
        if (optionalPage.isEmpty() || optionalTag.isEmpty()) throw new NotFoundException("Page or tag not found.");
        WikiTagRelation relation = WikiTagRelation.builder().page(optionalPage.get()).tag(optionalTag.get()).build();
        relationRepository.save(relation);
    }

    public void createRelations(Long pageId, TagListDto tagList) {
        for (TagDto tag : tagList.getTags()) {
            try {
                createRelation(pageId, tag.getId());
            } catch (NotFoundException e) {
                log.error("A page-tag relation could not be created.", e);
            }
        }
    }

    public void deleteRelation(Long pageId, Long tagId) {
        Optional<WikiPage> pageOptional = wikiRepository.findById(pageId);
        Optional<WikiTag> tagOptional = tagRepository.findById(tagId);
        if (pageOptional.isEmpty() || tagOptional.isEmpty()) return;
        Optional<WikiTagRelation> optionalRelation = relationRepository.findWikiTagRelationByTagAndPage(tagOptional.get(), pageOptional.get());
        if (optionalRelation.isEmpty()) return;
        relationRepository.delete(optionalRelation.get());
    }
}
