package com.energybox.backendcodingchallenge.repository;

import java.util.List;

import com.energybox.backendcodingchallenge.domain.Reading;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface ReadingRepository extends Neo4jRepository<Reading, String> {

  @Query ("match (r:Reading)<-[]-(s:Sensor) where id(s) = $0 return r")
  List<Reading> findAllBySensorId(Long id);
  
}
