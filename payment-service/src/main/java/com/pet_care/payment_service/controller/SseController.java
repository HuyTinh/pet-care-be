package com.pet_care.payment_service.controller;

import com.pet_care.payment_service.service.SseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SseController {

    SseService sseService;

    @GetMapping("/stream")
    public SseEmitter streamEvents(@RequestParam Long channel) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseService.emitters.put(channel, emitter);

        // Xóa emitter khi hoàn thành hoặc khi có lỗi
        emitter.onCompletion(() -> sseService.emitters.remove(channel));
        emitter.onTimeout(() -> sseService.emitters.remove(channel));
        emitter.onError((e) -> sseService.emitters.remove(channel));

        return emitter;
    }
}
