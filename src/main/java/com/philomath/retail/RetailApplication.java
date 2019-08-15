package com.philomath.retail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * https://howtodoinjava.com/spring-cloud/spring-boot-ribbon-eureka/
 *
 * Hystrix:
 * 1.
 * http://localhost:8761/actuator/hystrix.stream
 *  * It’s a continuous stream that Hystrix generates. It is just a health check result along with all the service calls that are being monitored by Hystrix.
 *  2.
 *  http://localhost:8761/hystrix
 *  This is visual dashboard initial state.
 *  After you hit a hystrix command enabled url like the following, you will start getting metrics in above two urls.
 *  http://localhost:8761/client/frontend/hello
 *
 *  https://dzone.com/articles/deploying-microservices-spring-cloud-vs-kubernetes
 *
 */


@SpringBootApplication
@EnableEurekaServer
@EnableWebSecurity
@EnableDiscoveryClient
@EnableEurekaClient
@RibbonClient(name = "eureka-serviceregistry", configuration = RibbonConfiguration.class)
@EnableZuulProxy
@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableConfigServer
public class RetailApplication
		extends WebSecurityConfigurerAdapter
{

	public static void main(String[] args) {
		SpringApplication.run(RetailApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/").permitAll();
	}
	// Register Zuul Filters
	@Bean
	public PreFilter preFilter() {
		return new PreFilter();
	}
	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}
	@Bean
	public ErrorFilter errorFilter() {
		return new ErrorFilter();
	}
	@Bean
	public RouteFilter routeFilter() {
		return new RouteFilter();
	}



}


