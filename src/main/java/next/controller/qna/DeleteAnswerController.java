package next.controller.qna;

import core.jdbc.DataAccessException;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAnswerController extends AbstractController {
    private AnswerDao answerDao = new AnswerDao();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long answerId = Long.parseLong(req.getParameter("answerId"));

        ModelAndView modelAndView = jsonView();
        try {
            answerDao.delete(answerId);
            modelAndView.addObject("result", Result.ok());
        } catch (DataAccessException e) {
            modelAndView.addObject("result", Result.fail(e.getMessage()));
        }

        return jsonView();
    }
}
