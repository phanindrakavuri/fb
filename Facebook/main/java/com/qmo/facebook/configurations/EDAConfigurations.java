package com.qmo.facebook.configurations;

import org.springframework.context.annotation.*;
@Configuration
@ComponentScan("com.qmo.facebook")
@PropertySources(value = {@PropertySource("classpath:application.properties")})
public class EDAConfigurations {

}
