package ee.taltech.iti0302.robotiklubi.controller;

import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageCreationResponseDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.service.WikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class WikiController {

    private final WikiService wikiService;

    @GetMapping("/wiki/{id}")
    public WikiPageDto getWikiPage(@PathVariable("id") Long id) {
        return wikiService.getPageById(id);
    }

    @PostMapping("/wiki/create")
    public WikiPageCreationResponseDto createWikiPage(@RequestBody WikiPageDto wikiPageDto) {
        return wikiService.createPage(wikiPageDto);
    }
}
