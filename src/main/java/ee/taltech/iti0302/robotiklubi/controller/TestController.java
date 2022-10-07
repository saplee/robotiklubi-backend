package ee.taltech.iti0302.robotiklubi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("test")
    String getTest() {
        return "testing endpoint";
    }
}
