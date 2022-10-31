package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {
    private String forwardUrl;

    public ForwardController(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        if (forwardUrl == null) {
            throw new NullPointerException("이동할 URL을 입력하세요.");
        }
    }

    @Override
    public View execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new JspView(forwardUrl);
    }
}