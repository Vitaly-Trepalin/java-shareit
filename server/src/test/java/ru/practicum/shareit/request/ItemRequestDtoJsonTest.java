package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestResponseDto> jsonResponse;
    @Autowired
    private JacksonTester<ItemRequestResponseWithItemDto> jsonResponseWithItem;

    @Test
    void testItemRequestResponseDto() throws Exception {
        ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto(1L, "Description",
                1L, LocalDateTime.of(2024, 12, 31, 23, 59, 59));

        JsonContent<ItemRequestResponseDto> result = jsonResponse.write(itemRequestResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathNumberValue("$.requestor").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-12-31 23:59:59");
    }

    @Test
    void testItemRequestResponseWithItemDto() throws Exception {
        ItemResponseDto itemResponseDto = new ItemResponseDto(1L, "Item", "Description", true,
                1L, 1L);
        ItemRequestResponseWithItemDto itemRequestResponseWithItemDto = new ItemRequestResponseWithItemDto(1L,
                "Description", 2L,
                LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                List.of(itemResponseDto));

        JsonContent<ItemRequestResponseWithItemDto> result = jsonResponseWithItem.write(itemRequestResponseWithItemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathNumberValue("$.requestor").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2024-12-31 23:59:59");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
    }
}