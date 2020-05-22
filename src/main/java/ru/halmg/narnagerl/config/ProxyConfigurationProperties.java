package ru.halmg.narnagerl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotNull;

@Data
@Profile("proxy")
@Configuration
@ConfigurationProperties(prefix = "proxy")
public class ProxyConfigurationProperties {
    @NotNull
    private String host;
    @NotNull
    private Integer port;
    private String login;
    private String password;
}
