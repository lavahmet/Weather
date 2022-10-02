package org.example;

import org.example.dto.MeasurementDTO;
import org.example.dto.MeasurementResponse;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawChart {
    public static void main(String[] args) {
        final String name = "SensorName1";
        List<Double> temperatures = getTemperaturesFromServer(name);
        drawChart(temperatures);

    }

    private static List<Double> getTemperaturesFromServer(String name) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8080/meaurements/" + name;

        MeasurementResponse jsonResponse = restTemplate.getForObject(url, MeasurementResponse.class);

        if (jsonResponse == null || jsonResponse.getMeasurements() == null) {
            return Collections.emptyList();
        }

        return jsonResponse.getMeasurements().stream().map(MeasurementDTO::getValue).collect(Collectors.toList());
    }

    private static void drawChart(List<Double> temparaturs) {
        double[] xData = IntStream.range(0, temparaturs.size()).asDoubleStream().toArray();
        double[] yData = temparaturs.stream().mapToDouble(x -> x).toArray();

        XYChart chart = QuickChart.getChart("Temparutes", "X", "Y", "temparature",
                xData, yData);

        new SwingWrapper(chart).displayChart();
    }
}
