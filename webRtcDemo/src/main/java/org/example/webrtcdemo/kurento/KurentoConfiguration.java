package org.example.webrtcdemo.kurento;

import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KurentoConfiguration {
    @Value("${kms.url}")
    private String KMS_URL;

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create(KMS_URL);
    }
}
