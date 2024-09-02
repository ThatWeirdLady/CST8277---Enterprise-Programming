package org.ac.cst8277.hailey.jennifer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Message {
    @Id
    private String id;
    private String content;
    private String producerId;

    public Message() {
    };

    public Message(String id, String content, String producerId) {
        this.id = id;
        this.content = content;
        this.producerId = producerId;
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

    /**
     * @return String return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
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

}
