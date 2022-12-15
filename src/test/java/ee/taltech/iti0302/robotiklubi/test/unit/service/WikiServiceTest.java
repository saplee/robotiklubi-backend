package ee.taltech.iti0302.robotiklubi.test.unit.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;


import ee.taltech.iti0302.robotiklubi.exception.InternalServerException;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.*;
import ee.taltech.iti0302.robotiklubi.repository.*;

import ee.taltech.iti0302.robotiklubi.service.WikiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class WikiServiceTest {

    @Mock
    private WikiRepository wikiRepository;
    @Mock
    private WikiTagRelationRepository wikiTagRelationRepository;
    @Spy
    private WikiPageMapper wikiPageMapper = new WikiPageMapperImpl();
    @Spy
    private WikiTagMapper wikiTagMapper = new WikiTagMapperImpl();
    @Spy
    private WikiPageMetaDataMapper wikiPageMetaDataMapper = new WikiPageMetaDataMapperImpl();


    @InjectMocks
    private WikiService wikiService;

    @Test
    void getPageById() {
        long id = 420L;
        String title = "This is the title";
        String content = "This is some very interesting content.";
        // given
        WikiPage wikiPage = WikiPage.builder().id(id).title(title).content(content).build();
        given(wikiRepository.findById(id)).willReturn(Optional.of(wikiPage));
        // when
        WikiPageDto result = wikiService.getPageById(id);
        // then
        then(wikiPageMapper).should().toDto(wikiPage);
        then(wikiRepository).should().findById(id);
        assertEquals(WikiPageDto.builder().id(id).title(title).content(content).build(), result);
    }

    @Test
    void getPagesByIdWrong() {
        long id = 222L;
        String title = "This is the title";
        String content = "This is some very interesting content.";
        // given
        Exception e = new Exception();
        given(wikiRepository.findById(233L)).willThrow(new InternalServerException("Could not retrieve wiki page (id " + 233 + ").", e));
        // when
        try {
            wikiService.getPageById(233L);
        } catch (InternalServerException internalServerException) {
            assertEquals("Could not retrieve wiki page (id " + 233 + ").", internalServerException.getMessage());
            assertThrows(InternalServerException.class, () -> wikiService.getPageById(233L));
        }
    }

    @Test
    void getPageTags() {
        long id2 = 230L;
        String title = "This is the title";
        String content = "This is some very interesting content.";
        WikiTag wikitag = WikiTag.builder().tag("Game").id(2L).build();
        WikiPage wikiPage = WikiPage.builder().id(id2).title(title).content(content).build();
        WikiTagRelation wikiTagRelation = WikiTagRelation.builder().id(1L).page(wikiPage).tag(wikitag).build();
        TagDto tagDto = TagDto.builder().tag("Game").id(2L).build();
        // given
        given(wikiTagRelationRepository.findAllByPageId(id2)).willReturn(new ArrayList<>(List.of(wikiTagRelation)));
        // then
        List<TagDto> result = new ArrayList<>(List.of(tagDto));
        List<TagDto> actual = wikiService.getPageTags(id2);
        then(wikiTagMapper).should().toDto(wikitag);
        then(wikiTagMapper).should().toDtoList(new ArrayList<>(List.of(wikitag)));
        then(wikiTagRelationRepository).should().findAllByPageId(id2);
        assertEquals(result.get(0), actual.get(0));
    }
}