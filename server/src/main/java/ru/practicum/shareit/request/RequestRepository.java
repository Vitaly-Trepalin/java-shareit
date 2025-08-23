package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(long requestor);

    List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(long requestor);

    @Query("SELECT r.id FROM ItemRequest r WHERE r.requestor.id = :requestorId")
    List<Long> findRequestIdsByRequestorId(@Param("requestorId") Long requestorId);
}