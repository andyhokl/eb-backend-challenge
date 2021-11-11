package com.energybox.backendcodingchallenge.domain;

import java.util.HashSet;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Gateway {
  @Id
  @GeneratedValue
  private Long id;

  public String alias;
  public String description;

  @Relationship(type = "CONNECTED_TO", direction = Relationship.Direction.INCOMING)
  private HashSet<Sensor> sensors = new HashSet<>();

  public Gateway(String alias, String description) {
    this.alias = alias;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void newSensor(Sensor sensor) {
    sensors.add(sensor);
  }

  public void removeSensor(Sensor sensor) {
    sensors.remove(sensor);
  }

}
