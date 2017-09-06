package com.example.springboot.sandbox.infrastructure.entry.cl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@ConfigurationProperties(prefix = "example.like.userAgentAlternative")
public class UserAgentAlternative {
    public Boolean local;
    public Integer defaultSutWaitInSeconds;
    public Map<String, Agent> agents;
}
