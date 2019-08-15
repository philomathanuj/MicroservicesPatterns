package com.philomath.retail;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class MyRestController {

    @Autowired
    Environment environment;

    @HystrixCommand(fallbackMethod = "callKeepAlive_Fallback")
    @GetMapping("/keep-alive")
    public String health() {
        return "I am Ok";
    }

    @GetMapping("/backend")
    @HystrixCommand(fallbackMethod = "callBE_Fallback")
    public String backend() {
        System.out.println("Inside MyRestController::backend...");

        String serverPort = environment.getProperty("local.server.port");

        System.out.println("Port : " + serverPort);

        return "Hello form Backend!!! " + " Host : localhost " + " :: Port : " + serverPort;
    }

    @SuppressWarnings("unused")
    private String callKeepAlive_Fallback() {

        System.out.println("keepalive service is down!! fallback route enabled...");

        return "CIRCUIT BREAKER ENABLED!!! No Response From backend Service at this moment. " +
                " Service will be back shortly - " + new Date();
    }

    @SuppressWarnings("unused")
    private String callBE_Fallback() {

        System.out.println("keepalive service is down!! fallback route enabled...");

        return "CIRCUIT BREAKER ENABLED!!! No Response From backend Service at this moment. " +
                " Service will be back shortly - " + new Date();
    }
}
