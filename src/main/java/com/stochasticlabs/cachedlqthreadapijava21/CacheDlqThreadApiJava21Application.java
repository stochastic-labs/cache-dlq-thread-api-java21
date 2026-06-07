package com.stochasticlabs.cachedlqthreadapijava21;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CacheDlqThreadApiJava21Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CacheDlqThreadApiJava21Application.class);

        String apiEnabled = System.getenv("APP_API_HTTP_ENABLED");
        if ("false".equalsIgnoreCase(apiEnabled)) {
            app.setWebApplicationType(WebApplicationType.NONE);
        }

        app.run(args);
    }
}
