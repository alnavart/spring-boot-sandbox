package com.example.springboot.sandbox.infrastructure.entry.cl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

@ActiveProfiles("override-issue")
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAgentOverrideIssueIntegrationTest {

    @Autowired
    UserAgent userAgent;

    @Test
    public void defaultPopulation(){
        List<Agent> expectedAgents = asList(androidAgent, desktopAgent);
        UserAgent expectedUserAgent = new UserAgent(false, 5, expectedAgents);

        assertThat(userAgent, not(equalTo(expectedUserAgent)));
    }

    private Agent androidAgent = new Agent(new Device("custom", "android"), new Os("", "android"));
    private Agent desktopAgent = new Agent(new Device("desktop", "windows"), null);
}