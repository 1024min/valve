package com.hiwei.valve;

import com.hiwei.valve.aspect.ValveAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ValveAspect.class)
@EnableConfigurationProperties(ValveProperties.class)
@ConditionalOnProperty(prefix = "spring.valve", name = "enabled", matchIfMissing = true)
public class ValveAutoConfiguration {
}
