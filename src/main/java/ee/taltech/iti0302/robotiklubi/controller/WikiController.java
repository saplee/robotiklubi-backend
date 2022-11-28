package ee.taltech.iti0302.robotiklubi.controller;

import ee.taltech.iti0302.robotiklubi.dto.wiki.*;
import ee.taltech.iti0302.robotiklubi.service.WikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class WikiController {

    private final WikiService wikiService;

    @PostMapping("/wiki/create")
    public void createWikiPage(@RequestBody WikiPageDto wikiPageDto) {
        wikiService.createPage(wikiPageDto);
    }

    @GetMapping("/wiki/{id}")
    public WikiPageDto getWikiPage(@PathVariable("id") Long id) {
        return wikiService.getPageById(id);
    }

    @PutMapping("/wiki/update")
    public void updateWikiPage(@RequestParam("id") Long id, @RequestBody WikiPageDto wikiPageDto) {
        wikiService.updatePage(id, wikiPageDto);
    }

    @DeleteMapping("/wiki/delete")
    public void deleteWikiPage(@RequestParam("id") Long id) {
        wikiService.deletePage(id);
    }

    @GetMapping("/wiki/tags/{id}")
    public List<TagDto> getWikiPageTags(@PathVariable("id") Long id) {
        return wikiService.getPageTags(id);
    }

    @PostMapping ("/wiki/search")
    public WikiSearchResult searchWikiPages(@RequestBody WikiSearchCriteria searchCriteria) {
        return wikiService.findAllByCriteria(searchCriteria);
    }
}
