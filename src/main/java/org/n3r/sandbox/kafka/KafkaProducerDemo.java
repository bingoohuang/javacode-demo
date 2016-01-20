package org.n3r.sandbox.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.Partitioner;
import kafka.producer.ProducerConfig;
import kafka.utils.VerifiableProperties;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

// http://kafka.apache.org/documentation.html#quickstart
// https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+Producer+Example
public class KafkaProducerDemo {
    public static void main(String[] args) {
        long events = 100;

        Properties props = new Properties();
        props.put("metadata.broker.list", "10.50.12.11:9092,10.50.12.12:9092,10.50.12.13:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", "org.n3r.sandbox.kafka.KafkaProducerDemo$SimplePartitioner");
        props.put("request.required.acks", "1");
        props.put("client.id", "bingoo.producer");
//        props.put("retry.backoff.ms", "30000");
//        props.put("reconnect.backoff.ms", "30000");

        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<>(config);
        long start = System.currentTimeMillis();

        for (long nEvents = 0; nEvents < events; nEvents++) {
            long runtime = new Date().getTime();
            String ip = "192.168.2." + (nEvents + start);
            String msg = runtime + ",www.example.com," + ip;
            KeyedMessage<String, String> data = new KeyedMessage<>("bingoo-visits", msg);
            producer.send(data);
        }

        producer.close();
    }


    public static class SimplePartitioner implements Partitioner {
        public SimplePartitioner(VerifiableProperties properties) {
        }

        @Override
        public int partition(Object key, int numPartitions) {
            int partition = 0;

            String keyStr = (String) key;
            int offset = keyStr.lastIndexOf('.');
            if (offset > 0) {
                partition = Integer.parseInt(keyStr.substring(offset + 1)) % numPartitions;
            }

            return partition;
        }
    }
}

