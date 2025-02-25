package pheninux.xdev.gestork.web.order.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static pheninux.xdev.gestork.utils.Utils.emitters;

@Controller
@RequestMapping("/public/order")
public class OrderPublicController {


    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter streamNotifications() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        try {
            emitter.send(SseEmitter.event().data("Connected to stream"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

}
