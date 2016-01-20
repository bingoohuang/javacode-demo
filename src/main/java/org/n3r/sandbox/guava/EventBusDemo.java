package org.n3r.sandbox.guava;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

// Class is typically registered by the container.
class EventBusChangeRecorder {
    @Subscribe
    public void recordCustomerChange(ChangeEvent e) {
        System.out.println(e.getMessage());
    }

}

class ChangeEvent {
    final private String message;

    public ChangeEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

public class EventBusDemo {
    public static void main(String[] args) {
        // somewhere during initialization
        EventBus eventBus = new EventBus();
        eventBus.register(new EventBusChangeRecorder());

        ChangeEvent event = new ChangeEvent("bingoo");
        eventBus.post(event);
    }
}
