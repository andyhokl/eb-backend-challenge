package com.energybox.backendcodingchallenge.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Sensor {
  @Id
  @GeneratedValue
  private Long id;

  public enum SensorType {
    TEMPERATURE,
    HUMIDITY,
    ELECTRICITY;
  }

  @Relationship(type = "CONNECTED_TO")
  private Gateway gateway;

  public HashSet<Sensor.SensorType> types;
  public String description;

  private HashMap<SensorType, Reading> last_readings = new HashMap<>();

  public Sensor(List<Sensor.SensorType> types, String description) {
    this.types = new HashSet<Sensor.SensorType>(types);
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setGateway(Gateway gateway) {
    this.gateway = gateway;
  }

  public Gateway isAssignedTo() {
    return gateway;
  }

  public Boolean updateReading(Reading reading) {
    if (!this.types.contains(reading.getType())) {
      return false;
    }
    this.last_readings.put(reading.getType(), reading);
    return true;
  } 

}
