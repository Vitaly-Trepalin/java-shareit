package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long userId);

    @Query("""
            SELECT i FROM Item i
            WHERE (LOWER(i.name) LIKE LOWER(concat('%', :text, '%'))
            OR LOWER(i.description) LIKE LOWER(concat('%', :text, '%')))
            AND i.available = true
            """)
    List<Item> findAllItemsByText(@Param("text") String text);

    List<Item> findAllItemByRequestId(long requestId);

    List<Item> findAllItemByRequestIdIn(List<Long> requestId);
}