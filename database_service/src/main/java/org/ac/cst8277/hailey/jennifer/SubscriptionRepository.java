package org.ac.cst8277.hailey.jennifer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    public List<Subscription> findByProducerId(String producerId);

    public List<Subscription> findBySubscriberId(String subscriberId);
}