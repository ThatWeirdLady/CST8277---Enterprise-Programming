package org.ac.cst8277.hailey.jennifer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    List<Member> findByUsername(String username);
}
