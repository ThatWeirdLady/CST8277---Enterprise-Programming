package org.ac.cst8277.hailey.jennifer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
    public List<Message> findByProducerId(String producerId);
}
