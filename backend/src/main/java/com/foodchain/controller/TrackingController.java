package com.foodchain.controller;

import com.foodchain.dto.TrackingResponse;
import com.foodchain.entity.TrackingHistory;
import com.foodchain.service.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<TrackingResponse> getTracking(@PathVariable String orderId) {
        return ResponseEntity.ok(trackingService.getTrackingByOrderId(orderId));
    }

    @GetMapping("/{orderId}/history")
    public ResponseEntity<List<TrackingHistory>> getTrackingHistory(@PathVariable String orderId) {
        return ResponseEntity.ok(trackingService.getTrackingHistory(orderId));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<TrackingResponse> updateTrackingStatus(@PathVariable String orderId,
                                                                 @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        return ResponseEntity.ok(trackingService.updateTrackingStatus(orderId, status));
    }
}
