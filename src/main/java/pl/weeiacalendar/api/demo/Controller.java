package pl.weeiacalendar.api.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/downloadICS")
public class Controller {
    @GetMapping
    String downloadICS()
    {
        return "Hello World";
    }
}
