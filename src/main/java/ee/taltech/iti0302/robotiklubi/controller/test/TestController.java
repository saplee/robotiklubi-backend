package ee.taltech.iti0302.robotiklubi.controller.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @GetMapping("test")
    String getTest() {
        return "testing endpoint";
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/test/login")
    String loginTest() {
        log.info("login test");
        return "successful test";
    }
}
