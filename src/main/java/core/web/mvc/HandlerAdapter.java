package core.web.mvc;

import core.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    boolean supports(Object handler);

    ModelAndView handler(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception;
}