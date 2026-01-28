package team.four.pas.services;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    private static final Long EMITTER_TIMEOUT = 30 * 60 * 1000L;

    private final Map<String, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(EMITTER_TIMEOUT);

        userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitter));

        emitter.onTimeout(() -> removeEmitter(userId, emitter));

        emitter.onError((e) -> removeEmitter(userId, emitter));

        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    public void sendNotification(String userId, String notificationMessage) {
        sendNotification(userId, "USER_NOTIFICATION", notificationMessage);
    }

    public void sendNotification(String userId, String notificationTitle, String notificationMessage) {
        List<SseEmitter> emitters = userEmitters.get(userId);

        if (emitters != null && !emitters.isEmpty()) {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name(notificationTitle)
                    .data(notificationMessage);

            emitters.forEach(emitter -> {
                try {
                    emitter.send(event);
                    System.out.println("Sent user notification: " + notificationMessage);
                } catch (IOException e) {
                    System.out.println("Error sending notification: " + e.getMessage());
                    emitter.completeWithError(e);
                    removeEmitter(userId, emitter);
                }
            });
        }
    }

    private void removeEmitter(String userId, SseEmitter emitter) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
            }
        }
    }
}