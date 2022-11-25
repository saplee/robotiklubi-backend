package ee.taltech.iti0302.robotiklubi.service;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMapper;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMapperImpl;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import ee.taltech.iti0302.robotiklubi.repository.WikiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class WikiServiceTest {

    @Mock
    private WikiRepository wikiRepository;

    @Spy
    private WikiPageMapper wikiPageMapper = new WikiPageMapperImpl();

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
}