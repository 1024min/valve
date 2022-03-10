package com.hiwei.valve;

import com.hiwei.valve.aspect.ValveAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ValveAspect.class)
public class ValveAutoConfiguration {
}
