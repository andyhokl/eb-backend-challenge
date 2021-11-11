package com.energybox.backendcodingchallenge.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.energybox.backendcodingchallenge.domain.Gateway;
import com.energybox.backendcodingchallenge.domain.Sensor;
import com.energybox.backendcodingchallenge.service.GatewayService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping( value = "/gateways" )
public class GatewayController {

    private final GatewayService service;

    public GatewayController( GatewayService service) {
        this.service = service;
    }

    @ApiOperation( value = "delete all data" )
    @RequestMapping( value = "", method = RequestMethod.DELETE )
    public ResponseEntity<Object> clean() throws IOException, InterruptedException {

        service.gatewayRepo.deleteAll();
        service.sensorRepo.deleteAll();

        return new ResponseEntity<>( HttpStatus.OK );
    }

    @ApiOperation( value = "get all gateways", response = Gateway.class, responseContainer = "List" )
    @RequestMapping( value = "", method = RequestMethod.GET )
    public ResponseEntity<List<Gateway>> list() throws IOException, InterruptedException {

        return new ResponseEntity<>( service.getAllGateways(), HttpStatus.OK );
    }

    @ApiOperation( value = "get gateway by id", response = Gateway.class)
    @RequestMapping( value = "/{id}", method = RequestMethod.GET )
    public ResponseEntity<Gateway> getGatewayById(
        @PathVariable Long id
    ) throws IOException, InterruptedException {

        Optional<Gateway> gatewayNode = service.gatewayRepo.findById(id);
        if (gatewayNode.isPresent()) {
            return new ResponseEntity<>( gatewayNode.get(), HttpStatus.OK );
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }

    @ApiOperation( value = "get gateway by sensor type", response = Gateway.class, responseContainer = "List")
    @RequestMapping( value = "/by-sensor-type/{type}", method = RequestMethod.GET )
    public ResponseEntity<List<Gateway>> getGatewayById(
        @PathVariable Sensor.SensorType type
    ) throws IOException, InterruptedException {
        
        return new ResponseEntity<>( service.gatewayRepo.findBySensorType(type), HttpStatus.OK );
    }

    @ApiOperation( value = "create a new sensor to gateway", response = Sensor.class)
    @RequestMapping( value = "/{id}/sensors", method = RequestMethod.POST )
    public ResponseEntity<Sensor> addSensor(
        @PathVariable Long id, @RequestBody Sensor sensor
    ) throws IOException, InterruptedException {

        Sensor newSensor = service.addSensor(sensor, id);
        if (newSensor != null) {
            return new ResponseEntity<>( newSensor, HttpStatus.OK );

        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }

    @ApiOperation( value = "assign an existing sensor to gateway", response = Sensor.class)
    @RequestMapping( value = "/{gatewayId}/sensors", method = RequestMethod.PUT )
    public ResponseEntity<Object> assignSensor(
        @PathVariable Long gatewayId, @RequestBody Long sensorId
    ) throws IOException, InterruptedException {

        Optional<Gateway> gatewayNode = service.gatewayRepo.findById(gatewayId);
        if (gatewayNode.isPresent()) {
            Optional<Sensor> sensorNode = service.sensorRepo.findById(sensorId);
            if (sensorNode.isPresent()) {
                Sensor sensor = sensorNode.get();
                Gateway gateway = gatewayNode.get();
                service.assignSensor(sensor, gateway);
                return new ResponseEntity<>( HttpStatus.OK );
            } else {
                return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
            }
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }

        
    }
}
