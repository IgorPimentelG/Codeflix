package com.fullcycle.admin.catalog;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test-integration")
@DataJpaTest
@ExtendWith(CleanUpExtension.class)
@ComponentScan(includeFilters = {
  @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
})
public @interface MySQLGatewayTest {}
