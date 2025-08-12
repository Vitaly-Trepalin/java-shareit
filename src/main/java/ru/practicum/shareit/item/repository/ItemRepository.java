package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwnerId(long userId);

    @Query("""
            SELECT i FROM Item i
            WHERE (LOWER(i.name) LIKE LOWER(concat('%', :text, '%'))
            OR LOWER(i.description) LIKE LOWER(concat('%', :text, '%')))
            AND i.available = true
            """)
    Collection<Item> findAllItemsByText(@Param("text") String text);
}