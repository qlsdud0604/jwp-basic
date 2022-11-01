package next.controller.user;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.ModelAndView;
import core.nmvc.AbstractNewController;
import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController extends AbstractNewController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserDao userDao = UserDao.getInstance();

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        ModelAndView mav = jspView("/user/list.jsp");
        mav.addObject("users", userDao.findAll());
        return mav;
    }

    @RequestMapping("/users/profile")
    public ModelAndView profile(HttpServletRequest req,  HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        ModelAndView mav = jspView("/user/profile.jsp");
        mav.addObject("user", userDao.findByUserId(userId));
        return mav;
    }

    @RequestMapping("/users/form")
    public ModelAndView form(HttpServletRequest req,  HttpServletResponse resp) throws Exception{
        return jspView("/user/form.jsp");
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req,  HttpServletResponse resp) throws Exception {
        User user = new User(
                req.getParameter("userId"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email")
        );
        log.debug("User : {}", user);
        userDao.insert(user);
        return jspView("redirect:/");
    }

    @RequestMapping("/users/updateForm")
    public ModelAndView updateForm(HttpServletRequest req,  HttpServletResponse resp) throws Exception {
        User user = userDao.findByUserId(req.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        ModelAndView mav = jspView("/user/updateForm.jsp");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req,  HttpServletResponse resp) throws Exception {
        User user = userDao.findByUserId(req.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User updateUser = new User(
                req.getParameter("userId"),
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email")
        );
        log.debug("Update User : {}", updateUser);
        userDao.update(updateUser);
        return jspView("redirect:/");
    }

    @RequestMapping("/users/loginForm")
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return jspView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req,  HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = userDao.findByUserId(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            return jspView("redirect:/");
        } else {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

    @RequestMapping("/users/logout")
    public ModelAndView logout(HttpServletRequest req,  HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute("user");
        return jspView("redirect:/qna/list");
    }
}
