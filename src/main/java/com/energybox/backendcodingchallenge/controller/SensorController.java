package com.energybox.backendcodingchallenge.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
@RequestMapping( value = "/sensors" )
public class SensorController {

    private final GatewayService service;

    public SensorController( GatewayService service ) {
        this.service = service;
    }

    @ApiOperation( value = "get all sensors", response = Sensor.class, responseContainer = "List" )
    @RequestMapping( value = "", method = RequestMethod.GET )
    public ResponseEntity<List<Sensor>> list() throws IOException, InterruptedException {

        return new ResponseEntity<>( service.getAllSensors(), HttpStatus.OK );
    }

    @ApiOperation( value = "get sensor by id", response = Sensor.class)
    @RequestMapping( value = "/{id}", method = RequestMethod.GET )
    public ResponseEntity<Sensor> getSensorById(
        @PathVariable Long id
    ) throws IOException, InterruptedException {

        Optional<Sensor> sensorNode = service.sensorRepo.findById(id);
        if (sensorNode.isPresent()) {
            return new ResponseEntity<>( sensorNode.get(), HttpStatus.OK );
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }

    @ApiOperation( value = "get sensor by gateway id", response = Sensor.class, responseContainer = "List")
    @RequestMapping( value = "/by-gateway/{id}", method = RequestMethod.GET )
    public ResponseEntity<List<Sensor>> getGatewayById(
        @PathVariable String id
    ) throws IOException, InterruptedException {

        Long gatewayId = Long.parseLong(id);
        
        return new ResponseEntity<>( service.getSensorsOfGateway(gatewayId), HttpStatus.OK );
    }

    @ApiOperation( value = "get sensor by sensor type", response = Sensor.class, responseContainer = "List")
    @RequestMapping( value = "/by-type/{type}", method = RequestMethod.GET )
    public ResponseEntity<List<Sensor>> getGatewayById(
        @PathVariable Sensor.SensorType type
    ) throws IOException, InterruptedException {

        // String typeString = type.toString();
        
        return new ResponseEntity<>( service.sensorRepo.findAllByType(type), HttpStatus.OK );
    }

    @ApiOperation( value = "create a sensor", response = Sensor.class)
    @RequestMapping( value = "", method = RequestMethod.POST )
    public ResponseEntity<Sensor> addSensor(
        @RequestBody Sensor sensor
    ) throws IOException, InterruptedException {

        Sensor newSensor = service.addSensor(sensor);
        if (newSensor != null) {
            return new ResponseEntity<>( newSensor, HttpStatus.OK );

        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }

    @ApiOperation( value = "remove assignment of a sensor", response = Sensor.class)
    @RequestMapping( value = "/{id}/assignment", method = RequestMethod.DELETE )
    public ResponseEntity<Object> removeAssignment(
        @PathVariable Long id
    ) throws IOException, InterruptedException {

        Optional<Sensor> sensorNode = service.sensorRepo.findById(id);
        if (sensorNode.isPresent()) {
            Sensor sensor = sensorNode.get();
            Boolean success = service.deallocateSensor(sensor);
            if (success) {
                return new ResponseEntity<>( HttpStatus.OK );
            } else {
                return new ResponseEntity<>( HttpStatus.NOT_FOUND );
            }
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }

    // @ApiOperation( value = "update reading of certain type", response = Reading.class)
    // @RequestMapping( value = "/{sensorId}/reading/{sensorType}", method = RequestMethod.POST )
    // public ResponseEntity<Reading> addSensor(
    //     @PathVariable Long sensorId, @PathVariable Sensor.SensorType sensorType, @RequestBody Double value
    // ) throws IOException, InterruptedException {

    //     String sensorIdString = Long.toString(sensorId) + "_" + sensorType;
    //     Optional<Reading> readingNode = service.readingRepo.findById(sensorIdString);
    //     if (readingNode.isPresent()) {
    //         Reading reading = readingNode.get();
    //         reading.setValue(value);
    //         service.readingRepo.save(reading);
    //         return new ResponseEntity<>( reading, HttpStatus.OK );
    //     } else {
    //         Reading reading = new Reading(sensorId, sensorType, value);
    //         Optional<Sensor> sensorNode = service.sensorRepo.findById(sensorId);
    //         if (sensorNode.isPresent()) {
    //             Sensor sensor = sensorNode.get();
    //             Boolean updateSuccess = sensor.updateReading(reading);
    //             if (updateSuccess) {
    //                 reading.setSensor(sensor);
    //                 service.sensorRepo.save(sensor);
    //                 service.readingRepo.save(reading);
    //                 return new ResponseEntity<>( reading, HttpStatus.OK );
    //             } else {
    //                 return new ResponseEntity<>( HttpStatus.NOT_FOUND );
    //             }
    //         } else {
    //             return new ResponseEntity<>( HttpStatus.NOT_FOUND );
    //         }

    //     }

    //     // Optional<Sensor> sensorNode = service.sensorRepo.findById(sensorId);
    //     // if (sensorNode.isPresent()) {
    //     //     Reading reading = service.readingRepo.findBySensorType(sensorId, sensorType);
    //     //     if (reading != null) {
    //     //         reading.setValue(value);
    //     //         service.readingRepo.save(reading);
    //     //         return new ResponseEntity<>( reading, HttpStatus.OK );
    //     //     } else {
    //     //         Reading newReading = service.addReading(sensorNode.get(), sensorType, value);
    //     //         return new ResponseEntity<>( newReading, HttpStatus.OK );
    //     //     }
    //     // } else {
    //     //     return new ResponseEntity<>( HttpStatus.NOT_FOUND );
    //     // }
    // }

    // @ApiOperation( value = "get reading of certain type", response = Sensor.class)
    // @RequestMapping( value = "/{sensorId}/reading/{sensorType}", method = RequestMethod.GET )
    // public ResponseEntity<Reading> readSensor(
    //     @PathVariable Long sensorId, @PathVariable Sensor.SensorType sensorType
    // ) throws IOException, InterruptedException {

    //     Optional<Sensor> sensorNode = service.sensorRepo.findById(sensorId);
    //     if (sensorNode.isPresent()) {
    //         Sensor sensor = sensorNode.get();
    //         return new ResponseEntity<>( sensor.getReading(sensorType), HttpStatus.OK );
    //     } else {
    //         return new ResponseEntity<>( HttpStatus.NOT_FOUND );
    //     }
    // }

    // @ApiOperation( value = "retrieva all readings of a sensor", response = Reading.class)
    // @RequestMapping( value = "/{id}/readings", method = RequestMethod.GET )
    // public ResponseEntity<List<Reading>> listReadings(
    //     @PathVariable Long id
    // ) throws IOException, InterruptedException {
    //     Optional<Sensor> sensorNode = service.sensorRepo.findById(id);
    //     if (sensorNode.isPresent()) {
    //         Sensor sensor = sensorNode.get();
    //         return new ResponseEntity<>( sensor.getAllReadings(), HttpStatus.OK );
    //     } else {
    //         return new ResponseEntity<>( HttpStatus.NOT_FOUND );
    //     }
    // }

}
