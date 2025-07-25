package com.knot.gateway.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomErrorController implements ErrorController {
    public String error() {
        return "error";
    }
}
