package com.helospark.FakeSsh;

import com.helospark.lightdi.annotation.ComponentScan;
import com.helospark.lightdi.annotation.Configuration;
import com.helospark.lightdi.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource(value = "file:appConfig.properties", ignoreResourceNotFound = true, order = -1)
@PropertySource(value = "classpath:ssh-properties.properties", ignoreResourceNotFound = false, order = 0)
public class FakeSshConfiguration {

}
