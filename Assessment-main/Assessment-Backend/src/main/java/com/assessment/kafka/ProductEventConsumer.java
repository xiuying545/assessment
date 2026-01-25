package com.assessment.kafka;

import com.assessment.models.Customer;
import com.assessment.models.Product;
import com.assessment.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import java.util.List;

@Slf4j
@Service
public class ProductEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventConsumer.class);

    @Autowired
    private CustomerRepository customerRepository;


    @KafkaListener(topics = "product-created", groupId = "product-group")
    public void consumeProductCreated(Product product) {
        log.info("Received product.created event: id={}, title={}", product.getId(), product.getBookTitle());
        // Fetch all customers
        List<Customer> customers = customerRepository.findAll();

        // Send email to each customer
        customers.forEach(customer -> {
            try {
                sendEmail(customer.getPersonalEmail(), product);
            } catch (Exception e) {
                log.error("Failed to send email to {} → {}", customer.getOfficeEmail(), e.getMessage(), e);
            }
        });
    }


    private void sendEmail(String toEmail, Product product) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Product Available: " + product.getBookTitle());
        message.setText("Hello!\n\nWe have a new product available: "
                + product.getBookTitle() + "\nPrice: "
                + product.getBookPrice() + "\nQuantity: "
                + product.getBookQuantity() + "\n\nCheck it out now!");
        log.info("Email sent to {}", toEmail);
    }
}