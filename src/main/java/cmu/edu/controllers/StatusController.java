package cmu.edu.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Status endpoint controller
@RestController
public class StatusController {
    @GetMapping("/status")
    public ResponseEntity<Object> getStatus() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
