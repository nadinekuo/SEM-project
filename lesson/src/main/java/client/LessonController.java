package client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface LessonController {
    @RequestMapping("/greeting")
    String greeting();
}
