package ee.taltech.iti0302.robotiklubi.controller.wiki;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TagController {

    private final TagService tagService;

    @PreAuthorize("hasAuthority('MEMBER')")
    @PostMapping("/tags/create")
    public void createTag(@RequestBody TagDto tagDto) {
        tagService.createTag(tagDto);
    }

    @GetMapping("/tags/all")
    public List<TagDto> getAllTags() {
        return tagService.getAllTags();
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @PutMapping("/tags/update")
    public void updateTag(@RequestParam("id") Long id, @RequestBody TagDto tagDto) {
        tagService.updateTag(id, tagDto);
    }

    @PreAuthorize("hasAuthority('MEMBER')")
    @DeleteMapping("/tags/delete")
    public void deleteTag(@RequestParam("id") Long id) {
        tagService.deleteTag(id);
    }
}
