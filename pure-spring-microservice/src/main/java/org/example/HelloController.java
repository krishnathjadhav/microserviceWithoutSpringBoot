package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
//@Slf4j
public class HelloController {
   RestoService restoService;

    public HelloController(RestoService restoService){
        this.restoService= restoService;
    }
    @GetMapping("/api/status")
    public Map<String, String> getStatus() {
        //log.info("Hello Krishnath: Status endpoint was invoked using Lombok @Slf4j!");
        return Map.of("status", "UP", "framework", "Krishnath: This is Pure Spring Framework", "Food", restoService.getFood());
    }

    // ONLY authenticated users can access this endpoint
    @Secured
    @GetMapping("/api/private/data")
    public Map<String, String> getPrivateData() {
        return Map.of("data", "Top Secret Restaurant Revenue Analytics");
    }
}
