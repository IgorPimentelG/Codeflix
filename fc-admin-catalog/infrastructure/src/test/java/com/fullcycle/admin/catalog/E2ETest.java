package com.fullcycle.admin.catalog;

import com.fullcycle.admin.catalog.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test-e2e")
@ExtendWith(CleanUpExtension.class)
@SpringBootTest(classes = WebServerConfig.class)
@AutoConfigureMockMvc
public @interface E2ETest {}
