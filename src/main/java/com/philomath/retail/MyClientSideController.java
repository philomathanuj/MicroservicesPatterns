package com.philomath.retail;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * This is to test ribbon client side load balancing
 *
 * http://localhost:8761/client/frontend/hello
 * redirects to /backend
 * then zuul redirects to /keep-alive
 */
@RestController
public class MyClientSideController {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/client/frontend/{id}")
    @HystrixCommand(fallbackMethod = "callBackendService_Fallback")
    public String callBackendService(@PathVariable String id) {
        String randomString = this.restTemplate.getForObject("http://eureka-serviceregistry/backend", String.class);
        return "Server Response :: " + randomString;
    }

    @SuppressWarnings("unused")
    private String callBackendService_Fallback(String schoolname) {

        System.out.println("backend service is down!! fallback route enabled...");

        return "CIRCUIT BREAKER ENABLED!!! No Response From backend Service at this moment. " +
                " Service will be back shortly - " + new Date();
    }
}
