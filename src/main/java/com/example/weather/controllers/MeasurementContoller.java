package com.example.weather.controllers;

import com.example.weather.dto.MeasurementDTO;
import com.example.weather.dto.MeasurementResponse;
import com.example.weather.models.Measurement;
import com.example.weather.services.MeasurementService;
import com.example.weather.util.MeasurementErrorResponse;
import com.example.weather.util.MeasurementException;
import com.example.weather.util.MeasurementValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.stream.Collectors;

import static com.example.weather.util.ErrorUtil.returnErrorToClient;

@RestController
@RequestMapping("/meaurements")
public class MeasurementContoller {

    private final ModelMapper modelMapper;
    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementContoller(ModelMapper modelMapper, MeasurementService measurementService, MeasurementValidator measurementValidator) {
        this.modelMapper = modelMapper;
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                          BindingResult bindingResult) {
        Measurement measurement = converToMeasurement(measurementDTO);

        measurementValidator.validate(measurement, bindingResult);

        if (bindingResult.hasErrors()) {
            returnErrorToClient(bindingResult);
        }

        measurementService.addMeasurement(measurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public MeasurementResponse getMeasurement() {
        return new MeasurementResponse(measurementService.findAll().stream().map(this::converToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{name}")
    public MeasurementResponse getMeasurmentBySensorName(@PathVariable("name") String name) {
        return new MeasurementResponse(measurementService.findBySensor(name).stream().map(this::converToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public Long getRainyDays() {
        return measurementService.findAll().stream().filter(Measurement::isRaining).count();
    }

    private Measurement converToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO converToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
