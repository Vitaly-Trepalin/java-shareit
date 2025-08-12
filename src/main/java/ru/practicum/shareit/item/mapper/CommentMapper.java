package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCreateCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class CommentMapper {
    public static Comment mapToComment(ItemCreateCommentDto itemCreateCommentDto, Item item, User user) {
        Comment comment = new Comment();
        comment.setText(itemCreateCommentDto.getText());
        comment.setItem(item);
        comment.setUser(user);
        return comment;
    }

    public static CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getUser().getName());
        commentResponseDto.setCreated(comment.getCreated());
        return commentResponseDto;
    }
}
