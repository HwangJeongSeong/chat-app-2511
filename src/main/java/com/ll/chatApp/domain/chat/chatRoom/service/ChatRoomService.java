package com.ll.chatApp.domain.chat.chatRoom.service;

import com.ll.chatApp.domain.chat.chatRoom.entity.ChatRoom;
import com.ll.chatApp.domain.chat.chatRoom.repository.ChatRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom create(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다. id=" + id));
    }

    @Transactional
    public void delete(Long id) {
        ChatRoom chatRoom = findById(id);
        chatRoomRepository.delete(chatRoom);
    }
}
