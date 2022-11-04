package core.web.oldmvc;

import core.web.mvc.HandlerAdapter;
import core.web.oldmvc.Controller;
import core.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handler(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        return ((Controller)handler).execute(req, resp);
    }
}
