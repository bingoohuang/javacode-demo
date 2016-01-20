package org.n3r.sandbox.guava;

import com.google.common.eventbus.Subscribe;

public class IntegerListener {

    private Integer lastMessage;

    @Subscribe
    public void listen(Integer integer) {
        lastMessage = integer;
    }

    public Integer getLastMessage() {
        return lastMessage;
    }
}