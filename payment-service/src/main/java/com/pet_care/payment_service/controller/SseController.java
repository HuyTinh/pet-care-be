package com.pet_care.payment_service.controller;

import com.pet_care.payment_service.service.SseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SseController {

    SseService sseService;


    @Scheduled(fixedRate = 1000)
    public void sseEvent() throws IOException {
        if(sseService.emitters.get(24L) != null)
        {
            sseService.emitters.get(24L).send("Chào");
        }
    }

    @GetMapping("/stream")
    public SseEmitter streamEvents(@RequestParam Long channel) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseService.emitters.put(channel, emitter);

        System.out.println("Chào");

        // Xóa emitter khi hoàn thành hoặc khi có lỗi
        emitter.onCompletion(() -> sseService.emitters.remove(channel));
        emitter.onTimeout(() -> sseService.emitters.remove(channel));
        emitter.onError((e) -> sseService.emitters.remove(channel));

        return emitter;
    }
}
