package com.example.weather.services;

import com.example.weather.models.Measurement;
import com.example.weather.models.Sensor;
import com.example.weather.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final SensorService sensorService;

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(SensorService sensorService, MeasurementRepository measurementRepository) {
        this.sensorService = sensorService;
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public List<Measurement> findBySensor(String name) {
        Optional<Sensor> sensor = sensorService.getSensorName(name);
        return measurementRepository.findBySensor(sensor);
    }

    @Transactional
    public void addMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    public void enrichMeasurement(Measurement measurement) {
        measurement.setSensor(sensorService.getSensorName(measurement.getSensor().getName()).get());

        measurement.setMeasurementDateTime(LocalDateTime.now());
    }
}
