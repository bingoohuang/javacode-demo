package org.n3r.sandbox.reactor.quickstart;


import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jon Brisbin
 */
public class TradeServer {

    private static final Random RANDOM = new Random();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int LEN = CHARS.length();
    private static final String[] SYMBOLS = new String[1000];

    static {
        for (int i = 0; i < SYMBOLS.length; i++) {
            SYMBOLS[i] = nextSymbol();
        }
    }

    private final AtomicLong counter = new AtomicLong();
    private final Thread queueDrain = new Thread() {
        @Override
        public void run() {
            while (active.get()) {
                try {
                    // Pull Orders off the queue and process them
                    buys.poll(100, TimeUnit.MILLISECONDS);
                    sells.poll(100, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                }
            }
        }
    };
    final BlockingQueue<Order> buys = new LinkedTransferQueue<Order>();
    final BlockingQueue<Order> sells = new LinkedTransferQueue<Order>();
    final AtomicBoolean active = new AtomicBoolean(true);

    public TradeServer() {
        queueDrain.start();
    }

    public Order execute(Trade trade) {
        Order o = new Order(counter.incrementAndGet())
                .setTrade(trade)
                .setTimestamp(System.currentTimeMillis());

        switch (trade.getType()) {
            case BUY:
                buys.add(o);
                break;
            case SELL:
                sells.add(o);
                break;
        }

        return o;
    }

    public Trade nextTrade() {
        return new Trade(counter.incrementAndGet())
                .setSymbol(SYMBOLS[RANDOM.nextInt(SYMBOLS.length)])
                .setQuantity(RANDOM.nextInt(500))
                .setPrice(Float.parseFloat(RANDOM.nextInt(700) + "." + RANDOM.nextInt(99)))
                .setType((RANDOM.nextInt() % 2 == 0 ? Type.BUY : Type.SELL));
    }

    public void stop() {
        active.set(false);
    }

    private static String nextSymbol() {
        char[] chars = new char[4];
        for (int i = 0; i < 4; i++) {
            chars[i] = CHARS.charAt(RANDOM.nextInt(LEN));
        }
        return new String(chars);
    }

}
