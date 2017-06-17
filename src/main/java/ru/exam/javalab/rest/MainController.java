package ru.exam.javalab.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by unlim_000 on 17.06.2017.
 */
@Controller("/")
public class MainController {

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
