package com.energybox.backendcodingchallenge.repository;

import java.util.List;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface GatewayRepository extends Neo4jRepository<Gateway, Long> {

  @Query ("match (s:Sensor)-[c:CONNECTED_TO]-(g:Gateway) where $0 in s.types return g")
  List<Gateway> findBySensorType(Sensor.SensorType type);
  // List<Person> findByTeammatesName(String name);
  // List<Person> findByTeammatesName(String name);
}