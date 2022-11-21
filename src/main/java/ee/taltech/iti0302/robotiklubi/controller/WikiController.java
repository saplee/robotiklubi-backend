package ee.taltech.iti0302.robotiklubi.controller;

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
    public void createWikiPage(@RequestBody WikiPageDto wikiPageDto) {
        wikiService.createPage(wikiPageDto);
    }

    @PutMapping("/wiki/update")
    public void updateWikiPage(@RequestParam("id") Long id, @RequestBody WikiPageDto wikiPageDto) {
        wikiService.updatePage(id, wikiPageDto);
    }

    @DeleteMapping("/wiki/delete")
    public void deleteWikiPage(@RequestParam("id") Long id) {
        wikiService.deletePage(id);
    }
}
