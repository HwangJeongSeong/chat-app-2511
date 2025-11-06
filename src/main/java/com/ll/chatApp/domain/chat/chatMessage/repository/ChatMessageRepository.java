package com.ll.chatApp.domain.chat.chatMessage.repository;

import com.ll.chatApp.domain.chat.chatMessage.entity.ChatMessage;
import com.ll.chatApp.domain.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderByIdAsc(ChatRoom chatRoom);

    List<ChatMessage> findByChatRoomAndIdGreaterThanOrderByIdAsc(ChatRoom chatRoom, long id);
}
