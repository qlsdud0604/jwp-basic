package core.web.oldmvc;

import core.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController extends AbstractController {
    private String forwardUrl;

    public ForwardController(String forwardUrl) {
        if (forwardUrl == null) {
            throw new NullPointerException("이동할 URL을 입력하세요.");
        }
        this.forwardUrl = forwardUrl;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView(forwardUrl);
    }

}