package com.hiwei.valve;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.valve")
public class ValveProperties {
    private Boolean enable = false;
    public ValveProperties() {
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
