package next.controller.qna;

import core.mvc.*;
import next.dao.AnswerDao;
import next.model.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddAnswerController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

    private AnswerDao answerDao = new AnswerDao();

    @Override
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Answer answer = new Answer(
                req.getParameter("writer"),
                req.getParameter("contents"),
                Long.parseLong(req.getParameter("questionId"))
        );
        log.debug("answer : {}", answer);

        Answer savedAnswer = answerDao.insert(answer);

        return jsonView().addObject("answer", savedAnswer);
    }
}