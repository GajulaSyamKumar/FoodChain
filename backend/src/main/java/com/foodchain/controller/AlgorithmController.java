package com.foodchain.controller;

import com.foodchain.dto.AlgorithmRequests.*;
import com.foodchain.dto.AlgorithmResponses.*;
import com.foodchain.service.AlgorithmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/algorithms")
public class AlgorithmController {

    private final AlgorithmService algorithmService;

    public AlgorithmController(AlgorithmService algorithmService) {
        this.algorithmService = algorithmService;
    }

    @PostMapping("/co1/execute")
    public ResponseEntity<CO1Response> executeCO1(@RequestBody CO1Request request) {
        return ResponseEntity.ok(algorithmService.executeCO1(request));
    }

    @PostMapping("/co2/execute")
    public ResponseEntity<CO2Response> executeCO2(@RequestBody CO2Request request) {
        return ResponseEntity.ok(algorithmService.executeCO2(request));
    }

    @PostMapping("/co3/execute")
    public ResponseEntity<CO3Response> executeCO3(@RequestBody CO3Request request) {
        return ResponseEntity.ok(algorithmService.executeCO3(request));
    }

    @PostMapping("/co4/execute")
    public ResponseEntity<CO4Response> executeCO4(@RequestBody CO4Request request) {
        return ResponseEntity.ok(algorithmService.executeCO4(request));
    }
}
