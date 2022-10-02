package com.example.weather.repositories;

import com.example.weather.models.Measurement;
import com.example.weather.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    List<Measurement> findBySensor(Optional<Sensor> sensor);
}
