package com.energybox.backendcodingchallenge.repository;

import java.util.List;

import com.energybox.backendcodingchallenge.domain.Sensor;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface SensorRepository extends Neo4jRepository<Sensor, Long> {

  @Query ("match (s:Sensor)-[c:CONNECTED_TO]-(g:Gateway) where id(g) = $0 return s")
  List<Sensor> findAllByGatewayId(Long id);

  @Query ("match (s:Sensor) where $0 in s.types return s")
  List<Sensor> findAllByType(Sensor.SensorType type);
}