package renovation.event.service.service.producer;

public interface KafkaProducer<K, V> {

    /**
     * Send data to Kafka broker(s) into default topic.
     *
     * @param data data for to be sent in Kafka
     */
    void send(V data);

    /**
     * Send data to Kafka broker(s) into default topic.
     *
     * @param key  key for to be sent in Kafka
     * @param data data for to be sent in Kafka
     */
    void send(K key, V data);

    /**
     * Send data to Kafka broker(s) into specified topic.
     *
     * @param key  key for to be sent in Kafka
     * @param data data for to be sent in Kafka
     * @param specifiedTopic topic for to be sent in Kafka
     */
    void send(K key, V data, String specifiedTopic);
}
