package com.example.onlineartstore.service;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// SSE (Server-Sent Events) - события отправленные сервером, SSE позволяет серверу отправлять обновления на клиентскую сторону в режиме реального времени!
@Service
public class SseEmitterService {
    private final Map<Integer, List<SseEmitter>> emitters = new ConcurrentHashMap<>();
    // map экземпляров SseEmitter

    public void addEmitter(SseEmitter emitter, int auctionId) {
        List<SseEmitter> list = emitters.computeIfAbsent(auctionId, k -> new CopyOnWriteArrayList<>());
        list.add(emitter);
        emitter.onCompletion(() -> {
            list.remove(emitter);
        });
    }

    // sendEventToAll отправляет события всем отправителям, отправляет событие всем незавершенным эмиттерам в списке
    // Если при отправке события возникает исключение, эмиттер помечается как завершенный и добавляется в список мертвых эмиттеров, которые будут удалены позже
    public void sendEventToAll(int auctionId, String eventName, Object eventData) {
        List<SseEmitter> emittersList = emitters.get(auctionId);
        if (emittersList != null) {
            for (SseEmitter emitter : emittersList) {
                try {
                    emitter.send(SseEmitter.event().name(eventName).data(eventData));
                } catch (IOException e) {
                    emittersList.remove(emitter);
                }
            }
        }
    }
}
