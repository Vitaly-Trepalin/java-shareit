package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(long userId, ItemCreateDto itemCreateDto) {
        return post("", userId, itemCreateDto);
    }

    public ResponseEntity<Object> findAllItemsByUserId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findByIdItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> updateItem(long itemId, long userId, ItemUpdateDto itemUpdateDto) {
        return patch("/" + itemId, userId, itemUpdateDto);
    }

    public ResponseEntity<Object> searchItems(String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", parameters);
    }

    public ResponseEntity<Object> createComment(long itemId, long userId, ItemCreateCommentDto itemCreateCommentDto) {
        return post("/" + itemId + "/comment", userId, itemCreateCommentDto);
    }
}