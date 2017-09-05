package com.example.springboot.sandbox.infrastructure.entry.cl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@ConfigurationProperties(prefix = "example.like.userAgent")
public class UserAgent {
    public Boolean local;
    public Integer defaultSutWaitInSeconds;
    public List<Agent> agents;
}
