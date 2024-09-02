package org.ac.cst8277.hailey.jennifer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Subscription {
    @Id
    private String id;
    private String producerId;
    private String subscriberId;

    public Subscription() {
    }

    public Subscription(String id, String producerId, String subscriberId) {
        this.id = id;
        this.producerId = producerId;
        this.subscriberId = subscriberId;
    }

    /**
     * @return String return the producerId
     */
    public String getProducerId() {
        return producerId;
    }

    /**
     * @param producerId the producerId to set
     */
    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    /**
     * @return String return the subscriberId
     */
    public String getSubscriberId() {
        return subscriberId;
    }

    /**
     * @param subscriberId the subscriberId to set
     */
    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
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
