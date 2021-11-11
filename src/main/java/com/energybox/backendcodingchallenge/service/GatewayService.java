package com.energybox.backendcodingchallenge.service;

import java.util.List;
import java.util.Optional;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.repository.GatewayRepository;
import com.energybox.backendcodingchallenge.repository.SensorRepository;

import org.springframework.stereotype.Service;

@Service
public class GatewayService {

  public final SensorRepository sensorRepo;
  public final GatewayRepository gatewayRepo;
  // public final ReadingRepository readingRepo;

  public GatewayService(SensorRepository sensorRepo, GatewayRepository gatewayRepo) {
  // public GatewayService(SensorRepository sensorRepo, GatewayRepository gatewayRepo, ReadingRepository readingRepo) {
    this.sensorRepo = sensorRepo;
    this.gatewayRepo = gatewayRepo;
    // this.readingRepo = readingRepo;
  }

  public Sensor addSensor(Sensor sensor) {
    sensorRepo.save(sensor);
    return sensor;
  }

  public void addGateway(Gateway gateway) {
    gatewayRepo.save(gateway);
  }

  public List<Sensor> getAllSensors() {
    return sensorRepo.findAll();
  }

  public List<Gateway> getAllGateways() {
    return gatewayRepo.findAll();
  }

  public Sensor addSensor(Sensor sensor, Long gatewayId) {
    Optional<Gateway> gatewayNode = gatewayRepo.findById(gatewayId);
    if (gatewayNode.isPresent()) {
      Gateway gateway = gatewayNode.get();

      sensor.setGateway(gateway);
      sensorRepo.save(sensor);

      gateway.newSensor(sensor);
      gatewayRepo.save(gateway);
      return sensor;
    } else {
      return null;
    }
  }

  public Boolean deallocateSensor(Sensor sensor) {
    Gateway oldGateway = sensor.isAssignedTo();
    if (oldGateway != null) {
      sensor.setGateway(null);
      oldGateway.removeSensor(sensor);
      sensorRepo.save(sensor);
      gatewayRepo.save(oldGateway);
      return true;
    }
    return false;
  }

  public void assignSensor(Sensor sensor, Gateway gateway) {
    deallocateSensor(sensor);
    sensor.setGateway(gateway);
    gateway.newSensor(sensor);
    sensorRepo.save(sensor);
    gatewayRepo.save(gateway);
  }

  public List<Sensor> getSensorsOfGateway(Long gatewayId) {
    return sensorRepo.findAllByGatewayId(gatewayId);
  };
}
