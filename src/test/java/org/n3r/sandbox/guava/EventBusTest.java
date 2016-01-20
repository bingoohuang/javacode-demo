package org.n3r.sandbox.guava;

import com.google.common.eventbus.EventBus;
import org.junit.Test;

import static com.mongodb.util.MyAsserts.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class EventBusTest {
    @Test
    public void shouldReceiveEvent() throws Exception {

        // given
        EventBus eventBus = new EventBus("test");
        EventListener listener = new EventListener();

        eventBus.register(listener);

        // when
        eventBus.post(new OurTestEvent(200));

        // then
        assertThat(listener.getLastMessage(), is(200));
    }

    @Test
    public void shouldReceiveMultipleEvents() throws Exception {

        // given
        EventBus eventBus = new EventBus("test");
        MultipleListener multiListener = new MultipleListener();

        eventBus.register(multiListener);

        // when
        eventBus.post(new Integer(100));
        eventBus.post(new Long(800));

        // then
        assertThat(multiListener.getLastInteger(), is(100));
        assertThat(multiListener.getLastLong(), is(800L));
    }

    @Test
    public void shouldDetectEventWithoutListeners() throws Exception {
        // given
        EventBus eventBus = new EventBus("test");

        DeadEventListener deadEventListener = new DeadEventListener();
        eventBus.register(deadEventListener);

        MultipleListener multiListener = new MultipleListener();
        eventBus.register(multiListener);

        // when
        eventBus.post(new Integer(100));

        // when
        eventBus.post(new OurTestEvent(200));

        assertThat(deadEventListener.isNotDelivered(), is(true));
    }

    @Test
    public void shouldGetEventsFromSubclass() throws Exception {

        // given
        EventBus eventBus = new EventBus("test");
        IntegerListener integerListener = new IntegerListener();
        NumberListener numberListener = new NumberListener();
        eventBus.register(integerListener);
        eventBus.register(numberListener);

        // when
        eventBus.post(new Integer(100));

        // then
        assertThat(integerListener.getLastMessage(), is(100));
        assertTrue(numberListener.getLastMessage().equals(100));

        //when
        eventBus.post(new Long(200L));

        // then
        // this one should has the old value as it listens only for Integers
        assertThat(integerListener.getLastMessage(), is(100));
        assertTrue(numberListener.getLastMessage().equals(200L));
    }
}
