package org.n3r.sandbox.kafka;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// https://cwiki.apache.org/confluence/display/KAFKA/Consumer+Group+Example
public class ConsumerGroupExample {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public ConsumerGroupExample(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
    }

    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, new Integer(a_numThreads));

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);

        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerThread(consumer, stream, threadNumber));
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "10000");
        props.put("zookeeper.sync.time.ms", "2000");
        props.put("auto.commit.interval.ms", "1000");
        props.put("client.id", "bingoo.consumer");

        return new ConsumerConfig(props);
    }

    public static void main(String[] args) {
        String zooKeeper = "10.50.12.11:2181,10.50.12.12:2181,10.50.12.13:2181";//args[0];
        String groupId = "demoGroupId"; //args[1];
       // String topic = "page.visits"; //args[2];
        String topic = "bingoo-visits"; //args[2];
        int threads = 1; //Integer.parseInt(args[3]);

        ConsumerGroupExample example = new ConsumerGroupExample(zooKeeper, groupId, topic);
        example.run(threads);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {

        }
        example.shutdown();
    }

    public static class ConsumerThread implements Runnable {
        private final ConsumerConnector m_consumer;
        private KafkaStream m_stream;
        private int m_threadNumber;

        public ConsumerThread(ConsumerConnector a_consumer, KafkaStream a_stream, int a_threadNumber) {
            m_consumer = a_consumer;
            m_threadNumber = a_threadNumber;
            m_stream = a_stream;
        }

        public void run() {
            ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
            int batch = 0;
            while (it.hasNext()) {
                System.out.println("Thread " + m_threadNumber + ": " + new String(it.next().message()));
                if (++batch == 100) {
                    m_consumer.commitOffsets();
                    batch = 0;
                }

            }
            System.out.println("Shutting down Thread: " + m_threadNumber);
        }
    }
}