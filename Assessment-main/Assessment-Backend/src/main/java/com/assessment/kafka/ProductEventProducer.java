package com.assessment.kafka;

import com.assessment.models.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventProducer.class);

    private final KafkaTemplate<String, Product> kafkaTemplate;

    private static final String TOPIC = "product-created";

    public void sendProductCreatedEvent(Product product) {
        kafkaTemplate.send(TOPIC, product);
        logger.info("Sent product.created event: {}", product.getId());
    }
}