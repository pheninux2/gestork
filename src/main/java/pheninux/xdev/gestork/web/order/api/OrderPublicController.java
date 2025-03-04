package pheninux.xdev.gestork.web.order.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static pheninux.xdev.gestork.utils.Utils.customerEmitters;
import static pheninux.xdev.gestork.utils.Utils.waiterEmitters;

@Controller
@RequestMapping("/public/order")
public class OrderPublicController {


    @GetMapping(value = "/created/stream", produces = "text/event-stream")
    public SseEmitter streamOrderCreatedNotification() {
        SseEmitter emitter = new SseEmitter();
        waiterEmitters.add(emitter);
        try {
            emitter.send(SseEmitter.event().data("Connected to stream"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        emitter.onCompletion(() -> waiterEmitters.remove(emitter));
        emitter.onTimeout(() -> waiterEmitters.remove(emitter));

        return emitter;
    }

    @GetMapping(value = "/status/stream", produces = "text/event-stream")
    public SseEmitter streamOrderStatusChangeNotification() {
        SseEmitter emitter = new SseEmitter();
        customerEmitters.add(emitter);
        try {
            emitter.send(SseEmitter.event().data("status changed"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        emitter.onCompletion(() -> customerEmitters.remove(emitter));
        emitter.onTimeout(() -> customerEmitters.remove(emitter));

        return emitter;
    }

}
