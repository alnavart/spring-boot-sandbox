package com.example.springboot.sandbox.infrastructure.entry.cl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@ActiveProfiles("override-alternative")
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAgentOverrideAlternativeIntegrationTest {

    @Autowired
    UserAgent userAgent;
    @Autowired
    UserAgentAlternative userAgentAlternative;

    @Test
    public void overrideByReferencedProperties(){
        List<Agent> expectedAgents = asList(androidAgent, desktopAgent);
        UserAgent expectedUserAgent = new UserAgent(false, 5, expectedAgents);

        assertThat(userAgent, equalTo(expectedUserAgent));
    }

    @Test
    public void overrideByChangeListToMap(){
        Map<String, Agent> expectedAgents = new HashMap<>();
        expectedAgents.put("agent0", androidAgent);
        expectedAgents.put("agent1", desktopAgent);
        UserAgentAlternative expectedUserAgentAlternative = new UserAgentAlternative(false, 5, expectedAgents);

        assertThat(userAgentAlternative, equalTo(expectedUserAgentAlternative));
    }

    private final Agent androidAgent = new Agent(new Device("custom", "androide"), new Os("", "android"));
    private final Agent desktopAgent = new Agent(new Device("desktop", "windows"), null);
}