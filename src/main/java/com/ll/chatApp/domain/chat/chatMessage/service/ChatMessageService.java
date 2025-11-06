package com.ll.chatApp.domain.chat.chatMessage.service;

import com.ll.chatApp.domain.chat.chatMessage.entity.ChatMessage;
import com.ll.chatApp.domain.chat.chatMessage.repository.ChatMessageRepository;
import com.ll.chatApp.domain.chat.chatRoom.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessage create(ChatRoom chatRoom, String writerName, String content) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .writerName(writerName)
                .content(content)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findByChatRoom(ChatRoom chatRoom) {
        return chatMessageRepository.findByChatRoomOrderByIdAsc(chatRoom);
    }

    public List<ChatMessage> findByChatRoomAfterId(ChatRoom chatRoom, long afterId) {
        if (afterId < 0) {
            return findByChatRoom(chatRoom);
        }
        return chatMessageRepository.findByChatRoomAndIdGreaterThanOrderByIdAsc(chatRoom, afterId);
    }
}
