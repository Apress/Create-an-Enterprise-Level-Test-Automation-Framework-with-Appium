package com.taf.testautomation;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@SuppressWarnings("rawtypes")
public class BaseTest {

    protected Session session = new Session();
    protected String sessionName;
    protected SessionManagement sessionManagement = new SessionManagement();
    protected HashMap<String, String> customProperties = session.getCustomProperties();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static String[][] dataTable;
    protected static List<String> sessionNames = Arrays.asList("Session1", "Session2", "Session3", "Session4", "Session5");

    @BeforeAll
    public void setUp() throws Exception {
        log("Initializing Session");
        session = startSession(sessionNames);
        sessionName = sessionManagement.getCurrentSessionName();
        log("Session created");
    }

    @AfterAll
    public void tearDown() throws Exception {
        log("Destroying Session");
        closeAllSessions();
        log("Session destroyed");
    }

    public void log(String message) {
        getLogger().info(message);
    }

    public void logError(String message) {
        getLogger().error(message);
    }

    public Session startSession(String sessionName) {
        try {
            sessionManagement.startSessionWithName(sessionName);
        } catch (Exception e) {
            logError("Error starting Session, trying again" + e.getMessage());
            sessionManagement.startSessionWithName(sessionName);
        }
        if (sessionManagement.getCurrentSession().getAppiumDriver() != null) {
            return sessionManagement.getCurrentSession();
        } else throw new IllegalArgumentException("Error starting Session:" + sessionName);
    }

    public Session startSession(List<String> sessionNames) {
        try {
            sessionManagement.startFirstAvailableSession(sessionNames);
        } catch (Exception e) {
            logError("Error starting Session, trying again" + e.getMessage());
            sessionManagement.startFirstAvailableSession(sessionNames);
        }
        if (sessionManagement.getCurrentSession().getAppiumDriver() != null) {
            return sessionManagement.getCurrentSession();
        } else throw new IllegalArgumentException("Error starting Session:");
    }

    public void closeSession(String sessionName) {
        try {
            sessionManagement.closeSession(sessionName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error closing Session:" + sessionName);
        }
    }

    public void closeAllSessions() {
        try {
            sessionManagement.closeAllSessions();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error closing Sessions:" + e.getMessage());
        }
    }
}
