package com.Project.LinkedIn.Connection_Service.Repository;

import com.Project.LinkedIn.Connection_Service.Entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepositories extends Neo4jRepository<Person,Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (personA:Person)-[:CONNECTED_TO]->(personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getFirstDegreeConnection(Long userId);

    @Query("MATCH (personA:Person)-[:CONNECTED_TO*2]->(personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getSecondDegreeConnection(Long userId);

    @Query("MATCH (personA:Person)-[:CONNECTED_TO*3]->(personB:Person) " +
            "WHERE personA.userId = $userId " +
            "RETURN personB")
    List<Person> getThirdDegreeConnection(Long userId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId,Long receiverId);

    @Query("MATCH (p1:Person)-[r:CONNECTED_TO]-(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId,Long receiverId);

    @Query("MATCH (p1:Person), (p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId,Long receiverId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person)" +
            "WHERE p1.userId =  $senderId AND p2.userId = $receiverId " +
            "DELETE r " +
            "CREATE (p1)-[:CONNECTED_TO]->(p2)")
    void AcceptRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "DELETE r")
    void RejectConnectionRequest(Long senderId, Long receiverId);
}
