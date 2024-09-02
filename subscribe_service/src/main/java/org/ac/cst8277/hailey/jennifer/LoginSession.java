package org.ac.cst8277.hailey.jennifer;

import java.util.UUID;

import java.time.Instant;

public class LoginSession {
    private String id;
    private String userId;
    private String token;
    private long validUntil;

    public LoginSession(String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.validUntil = Instant.now().getEpochSecond() + (60 * 60);

    }

    public LoginSession() {
    }

    /**
     * @return String return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return String return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return long return the validUntil
     */
    public long getValidUntil() {
        return validUntil;
    }

    /**
     * @param validUntil the validUntil to set
     */
    public void setValidUntil(long validUntil) {
        this.validUntil = validUntil;
    }

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
