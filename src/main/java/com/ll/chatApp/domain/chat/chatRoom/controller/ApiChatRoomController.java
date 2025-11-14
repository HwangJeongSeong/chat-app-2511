package com.ll.chatApp.domain.chat.chatRoom.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat/rooms")
public class ApiChatRoomController {
    @GetMapping
    public String getChatRooms() {
        return "채팅방 목록 조회 완료";
    }

    @GetMapping("/{roomId}")
    public String getChatRoom(@PathVariable("roomId") Long roomId) {
        return roomId + "번 채팅방 조회 완료";
    }

    @PostMapping
    public String createChatRoom() {
        return "채팅방 생성완료";
    }
}
