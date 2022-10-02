package com.example.weather.util;

import com.example.weather.models.Measurement;
import com.example.weather.services.MeasurementService;
import com.example.weather.services.SensorService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    public MeasurementValidator(SensorService measurementService) {
        this.sensorService = measurementService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;

        if (measurement.getSensor() == null) {
            return;
        }

        if (sensorService.getSensorName(measurement.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "No registered sensor with this name");
        }
    }

}
