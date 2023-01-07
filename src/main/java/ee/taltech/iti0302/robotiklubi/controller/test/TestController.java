package ee.taltech.iti0302.robotiklubi.controller.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("test")
    String getTest() {
        return "testing endpoint";
    }

}
