package ee.taltech.iti0302.robotiklubi.controller;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.service.WikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WikiController {

    private final WikiService wikiService;

    @GetMapping("/wiki/{id}")
    public WikiPageDto getWikiPage(@PathVariable("id") Long id) {
        return wikiService.getPageById(id);
    }

    @GetMapping("/wiki/tags/{id}")
    public List<TagDto> getWikiPageTags(@PathVariable("id") Long id) {
        return wikiService.getPageTags(id);
    }
}
