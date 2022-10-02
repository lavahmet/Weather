package com.example.weather.controllers;

import com.example.weather.dto.SensorDTO;
import com.example.weather.models.Sensor;
import com.example.weather.services.SensorService;
import com.example.weather.util.MeasurementErrorResponse;
import com.example.weather.util.MeasurementException;
import com.example.weather.util.SensorValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.weather.util.ErrorUtil.returnErrorToClient;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final ModelMapper modelMapper;
    private final SensorService sensorService;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(ModelMapper modelMapper, SensorService sensorService, SensorValidator sensorValidator) {
        this.modelMapper = modelMapper;
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                 BindingResult bindingResult) {
      Sensor sensor = convertToSensoer(sensorDTO);

      sensorValidator.validate(sensor, bindingResult);

      if (bindingResult.hasErrors()){
          returnErrorToClient(bindingResult);
      }

      sensorService.register(sensor);
      return ResponseEntity.ok(HttpStatus.OK);

    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    private Sensor convertToSensoer(SensorDTO sensorDTO) {
      return modelMapper.map(sensorDTO, Sensor.class);
    }
}
