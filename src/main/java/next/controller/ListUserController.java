package next.controller;

import core.mvc.Controller;
import next.dao.UserDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return "redirect:/users/loginForm";
        }

        UserDao userDao = new UserDao();
        request.setAttribute("users", userDao.findAll());
        return "/user/list.jsp";
    }
}