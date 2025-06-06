package org.farhan.greeting.repository;

import java.util.List;

import org.farhan.greeting.model.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GreetingRepository extends JpaRepository<Greeting, Long> {
    
    /**
     * Find all greetings ordered by creation time in descending order (newest first)
     * 
     * @return List of greetings sorted by time
     */
    @Query("SELECT g FROM Greeting g ORDER BY g.createdTime DESC")
    List<Greeting> findAllByOrderByCreatedTimeDesc();
}
