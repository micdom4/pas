package team.four.pas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import team.four.pas.services.NotificationService;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:[*]"})
@RequestMapping(value = {"/notifications"}, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe/{id}")
    @PreAuthorize(
            "(hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, T(team.four.pas.security.SecurityRoles).MANAGER)) " +
                    "|| (hasRole(T(team.four.pas.security.SecurityRoles).CLIENT) && @ownershipChecker.isOwner(authentication, #id))"
    )
    public SseEmitter subscribe(@PathVariable String id) {
        return notificationService.subscribe(id);
    }

    @PostMapping(value = "/trigger/{id}")
    @PreAuthorize("hasAnyRole(T(team.four.pas.security.SecurityRoles).ADMIN, T(team.four.pas.security.SecurityRoles).MANAGER)")
    public ResponseEntity<Void> triggerNotification(@PathVariable String id, @RequestParam String message) {
        notificationService.sendNotification(id, message);
        return ResponseEntity.ok().build();
    }
}