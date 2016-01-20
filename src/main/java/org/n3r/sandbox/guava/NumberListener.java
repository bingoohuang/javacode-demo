package org.n3r.sandbox.guava;

import com.google.common.eventbus.Subscribe;

public class NumberListener {

    private Number lastMessage;

    @Subscribe
    public void listen(Number integer) {
        lastMessage = integer;
    }

    public Number getLastMessage() {
        return lastMessage;
    }
}