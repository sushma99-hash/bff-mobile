package cmu.edu.controllers;//package cmu.edu.controllers;
//
////import client.CustomersClient;
////import errors.CustomFeignException;
//import cmu.edu.ds.Models.Customer;
//import cmu.edu.client.CustomersClient;
//import cmu.edu.ds.errors.CustomFeignException;
//import jakarta.validation.constraints.Email;
////import models.Customer;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//// Controller for Customers endpoints
//@RestController
//@RequestMapping("/customers")
//public class CustomersController {
//    private final CustomersClient customersClient;
//
//    public CustomersController(CustomersClient customersClient) {
//        this.customersClient = customersClient;
//    }
//
//    @PostMapping
//    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
//        try {
//            Object result = customersClient.addCustomer(customer);
//            return ResponseEntity.ok(result);
//        } catch (CustomFeignException e) {
//            try {
//                // Convert the response body to the appropriate format
////                String responseBody = StreamUtils.copyToString(e.getBody().asInputStream(), StandardCharsets.UTF_8);
//                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
//                return ResponseEntity.status(e.getStatus()).body(responseBody);
//            } catch (IOException ioException) {
//                // Fallback if we can't read the response body
//                return ResponseEntity.status(e.getStatus()).build();
//            }
//        }
//    }
//
////        @GetMapping
////        public ResponseEntity<Object> getCustomers(@RequestParam(required = false) String userId) {
////            if (userId != null) {
////                return ResponseEntity.ok(customersClient.getCustomerByUserId(userId));
////            }
////            return ResponseEntity.ok(customersClient.getAllCustomers());
////        }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getCustomerById(@PathVariable String id) {
//        try {
//            Object result = customersClient.getCustomerById(id);
//            return ResponseEntity.ok(result);
//        } catch (CustomFeignException e) {
//            try {
////                String responseBody = StreamUtils.copyToString(e.getBody().asInputStream(), StandardCharsets.UTF_8);
//                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
//                return ResponseEntity.status(e.getStatus()).body(responseBody);
//            } catch (IOException ioException) {
//                return ResponseEntity.status(e.getStatus()).build();
//            }
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<Object> getCustomerByUserId(@RequestParam("userId") @Email String userId) {
//        try {
//            Object result = customersClient.getCustomerByUserId(userId);
//            return ResponseEntity.ok(result);
//        } catch (CustomFeignException e) {
//            try {
////                String responseBody = StreamUtils.copyToString(e.getBody().asInputStream(), StandardCharsets.UTF_8);
//                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
//                return ResponseEntity.status(e.getStatus()).body(responseBody);
//            } catch (IOException ioException) {
//                return ResponseEntity.status(e.getStatus()).build();
//            }
//        }
//    }
//}


//package cmu.edu.ds.controllers;

import cmu.edu.ds.Models.Customer;
import cmu.edu.client.CustomersClient;
import cmu.edu.errors.CustomFeignException;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomersController {
    private final CustomersClient customersClient;

    public CustomersController(CustomersClient customersClient) {
        this.customersClient = customersClient;
    }

    @PostMapping
    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
        try {
            Object result = customersClient.addCustomer(customer);
            return ResponseEntity.ok(result);
        } catch (CustomFeignException e) {
            try {
                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
                return ResponseEntity.status(e.getStatus()).body(responseBody);
            } catch (IOException ioException) {
                return ResponseEntity.status(e.getStatus()).build();
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(
            @PathVariable String id,
            @RequestAttribute("clientType") String clientType) {
        try {
            Object result = customersClient.getCustomerById(id);
            return transformForMobile(result, clientType);
        } catch (CustomFeignException e) {
            try {
                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
                return ResponseEntity.status(e.getStatus()).body(responseBody);
            } catch (IOException ioException) {
                return ResponseEntity.status(e.getStatus()).build();
            }
        }
    }

    @GetMapping
    public ResponseEntity<Object> getCustomerByUserId(
            @RequestParam("userId") @Email String userId,
            @RequestAttribute("clientType") String clientType) {
        try {
            Object result = customersClient.getCustomerByUserId(userId);
            return transformForMobile(result, clientType);
        } catch (CustomFeignException e) {
            try {
                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
                return ResponseEntity.status(e.getStatus()).body(responseBody);
            } catch (IOException ioException) {
                return ResponseEntity.status(e.getStatus()).build();
            }
        }
    }

    private ResponseEntity<Object> transformForMobile(Object result, String clientType) {
        if (!(result instanceof Map) || clientType.equals("web")) {
            return ResponseEntity.ok(result);
        }

        // For mobile clients (iOS/Android), remove address fields
        Map<String, Object> responseMap = (Map<String, Object>) result;
        responseMap.remove("address");
        responseMap.remove("address2");
        responseMap.remove("city");
        responseMap.remove("state");
        responseMap.remove("zipcode");

        return ResponseEntity.ok(responseMap);
    }
}