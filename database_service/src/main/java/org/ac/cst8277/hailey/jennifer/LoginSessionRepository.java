package org.ac.cst8277.hailey.jennifer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSessionRepository extends JpaRepository<LoginSession, String> {
    LoginSession findByToken(String token);
}
