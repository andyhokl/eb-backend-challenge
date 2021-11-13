package com.energybox.backendcodingchallenge.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Reading {
  @Id
  private String id;

  private Long sensorId;

  private Sensor.SensorType type;

  private Long timestamp;
  private Double value;

  public Reading(Long sensorId, Sensor.SensorType type, Double value) {
    this.sensorId = sensorId;

    this.id = Long.toString(sensorId) + "_" + type;
    
    this.type = type;
    this.value = value;
    
    this.timestamp = System.currentTimeMillis();
  }
  
  public Long getSensorId() {
    return this.sensorId;
  }

  public Sensor.SensorType getType() {
    return this.type;
  }

  public Long getTimestamp() {
    return this.timestamp;
  }

  public Double getValue() {
    return this.value;
  };

  public void setValue(Double value) {
    this.value = value;
    this.timestamp = System.currentTimeMillis();
  }
  
}
