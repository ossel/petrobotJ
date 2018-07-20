package com.ossel.petrobot.services;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HttpServer {

    public static final Logger LOG = Logger.getLogger(HttpServer.class);

    @RequestMapping("/temperature")
    public String receiveTemperature(
            @RequestParam(value = "value", defaultValue = "") String value) {
        LOG.info("Temperature: " + value);
        Dao.getInstance().putTemperature(value);
        return "ok";
    }

}
