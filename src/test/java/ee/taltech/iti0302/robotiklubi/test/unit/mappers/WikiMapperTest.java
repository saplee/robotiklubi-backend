package ee.taltech.iti0302.robotiklubi.test.unit.mappers;

import ee.taltech.iti0302.robotiklubi.dto.wiki.TagDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageDto;
import ee.taltech.iti0302.robotiklubi.dto.wiki.WikiPageMetaDataDto;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMapper;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiPageMetaDataMapper;
import ee.taltech.iti0302.robotiklubi.mappers.wiki.WikiTagMapper;
import ee.taltech.iti0302.robotiklubi.repository.User;
import ee.taltech.iti0302.robotiklubi.repository.WikiPage;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WikiMapperTest {

    private final WikiPageMapper mapper = Mappers.getMapper(WikiPageMapper.class);
    private final WikiPageMetaDataMapper metaDataMapper = Mappers.getMapper(WikiPageMetaDataMapper.class);
    private final WikiTagMapper tagMapper = Mappers.getMapper(WikiTagMapper.class);

    @Test
    void orderToDtoTest() {
        User author = User.builder()
                .id(7L)
                .firstName("Aadu")
                .lastName("Beedu")
                .build();
        User editor = User.builder()
                .id(8L)
                .firstName("Edit")
                .lastName("Man")
                .build();
        OffsetDateTime createdTime = OffsetDateTime.now();
        OffsetDateTime editedTime = OffsetDateTime.of(2023, 1, 8, 6, 7, 2, 6, ZoneOffset.ofHours(0));
        WikiPage page = WikiPage.builder()
                .id(16L)
                .title("Wasd")
                .content("Qwertyui.")
                .author(author)
                .lastEditedBy(editor)
                .createdAt(createdTime)
                .lastEdited(editedTime)
                .build();
        WikiPageDto dto = mapper.toDto(page);
        assertEquals(16L, dto.getId());
        assertEquals("Wasd", dto.getTitle());
        assertEquals("Qwertyui.", dto.getContent());
        assertEquals(7, dto.getAuthorId());
        assertEquals("Aadu Beedu", dto.getAuthorName());
        assertEquals(8, dto.getLastEditorId());
        assertEquals("Edit Man", dto.getLastEditorName());
        assertEquals(createdTime, dto.getCreatedAt());
        assertEquals(editedTime, dto.getLastEdited());
    }

    @Test
    void orderToDtoAuthorEditorNullIdTest() {
        User author = User.builder()
                .firstName("Aadu")
                .lastName("Beedu")
                .build();
        User editor = User.builder()
                .firstName("Edit")
                .lastName("Man")
                .build();
        OffsetDateTime createdTime = OffsetDateTime.now();
        OffsetDateTime editedTime = OffsetDateTime.of(2023, 1, 8, 6, 7, 2, 6, ZoneOffset.ofHours(0));
        WikiPage page = WikiPage.builder()
                .id(16L)
                .title("Wasd")
                .content("Qwertyui.")
                .author(author)
                .lastEditedBy(editor)
                .createdAt(createdTime)
                .lastEdited(editedTime)
                .build();
        WikiPageDto dto = mapper.toDto(page);
        assertEquals(16L, dto.getId());
        assertEquals("Wasd", dto.getTitle());
        assertEquals("Qwertyui.", dto.getContent());
        assertNull(dto.getAuthorId());
        assertEquals("Aadu Beedu", dto.getAuthorName());
        assertNull(dto.getLastEditorId());
        assertEquals("Edit Man", dto.getLastEditorName());
        assertEquals(createdTime, dto.getCreatedAt());
        assertEquals(editedTime, dto.getLastEdited());
    }

    @Test
    void pageToDtoNullTest() {
        WikiPageDto dto = mapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void pageToMetaDataDtoNullTest() {
        WikiPageMetaDataDto dto = metaDataMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void pageToMetaDataDtoListNullTest() {
        List<WikiPageMetaDataDto> dto = metaDataMapper.toDtoList(null);
        assertNull(dto);
    }

    @Test
    void tagToDtoNullTest() {
        TagDto dto = tagMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void tagToDtoListNullTest() {
        List<TagDto> dto = tagMapper.toDtoList(null);
        assertNull(dto);
    }
}
