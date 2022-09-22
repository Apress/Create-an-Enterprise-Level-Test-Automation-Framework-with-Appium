package com.taf.testautomation;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class SessionManagement {

    private HashMap<String, Session> sessionList;
    private String currentSessionName;

    public SessionManagement() {
        this.sessionList = new HashMap<String, Session>();
    }

    public Session startSessionWithName(String name) {
        synchronized (this.sessionList) {
            if (this.sessionList.containsKey(name)) {
                throw new IllegalArgumentException("Session already started, please close Session before starting a new Session with same name:" + name);
            }
        }
        Session session = new Session();
        try {
            session.startSession();
            synchronized (this.sessionList) {
                this.sessionList.put(name, session);
            }
            this.setCurrentSessionName(name);
        } catch (Exception e) {
            log.info("Error starting session:" + "\n" + e);
            throw new RuntimeException(e);
        }
        return session;
    }

    public Session startFirstAvailableSession(List<String> sessionNames) {
        String sessionName = sessionNames.stream().filter(e -> !new ArrayList<String>(this.sessionList.keySet()).contains(e)).findFirst().orElse(null);
        Session session = new Session();
        try {
            session.startSession();
            synchronized (this.sessionList) {
                this.sessionList.put(sessionName, session);
            }
            this.setCurrentSessionName(sessionName);
        } catch (Exception e) {
            log.info("Error starting session:"+ "\n" + e);
            throw new RuntimeException(e);
        }
        log.info("Available Session names are " + new ArrayList<String>(this.sessionList.keySet()));
        return session;
    }

    public Session getCurrentSession() {
        String currentSessionName = this.getCurrentSessionName();
        return this.getSession(currentSessionName);
    }

    public Session getSession(String sessionName) {
        synchronized (this.sessionList) {
            return this.sessionList.get(this.currentSessionName);
        }
    }

    public void closeSession(String sessionName) {
        Session session;
        synchronized (this.sessionList) {
            session = this.sessionList.get(sessionName);
        }
        this.closeSession(sessionName, session);
    }

    private void closeSession(String name, Session session) {
        try {
            session.closeSession();
            synchronized (this.sessionList) {
                this.sessionList.remove(name);
            }
        } catch (Exception e) {
            log.info("Error closing session", e);
        }
    }

    public void closeAllSessions() {
        List<String> sessions;
        synchronized (this.sessionList) {
            sessions = new ArrayList<String>(this.sessionList.keySet());
        }
        sessions.parallelStream().forEach((Consumer<? super Object>) e -> closeSession((String) e));
    }

    public HashMap<String, Session> getSessionList() {
        return this.sessionList;
    }

    public String getCurrentSessionName() {
        return this.currentSessionName;
    }

    public void setSessionList(HashMap<String, Session> sessionList) {
        this.sessionList = sessionList;
    }

    public void setCurrentSessionName(String currentSessionName) {
        this.currentSessionName = currentSessionName;
    }
}
