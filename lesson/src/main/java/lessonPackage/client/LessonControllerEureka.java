package lessonPackage.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface LessonControllerEureka {
    @RequestMapping("/greeting")
    String greeting();
}
