package core.web.oldmvc;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.web.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping("/users/findUserId")
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("findUserId");
        return null;
    }

    @RequestMapping(value = "/users/findUserId", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("save");
        return null;
    }

}
