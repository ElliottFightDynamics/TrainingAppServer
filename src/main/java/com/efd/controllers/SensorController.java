package com.efd.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by volodymyr on 14.06.17.
 */
@Controller("EFD/sensor")
public class SensorController {

    @RequestMapping(value = "/isUnclaimedSensors?", method = RequestMethod.POST)
    public String SensorRegistrationStatus() {
        return null;
    }

}
