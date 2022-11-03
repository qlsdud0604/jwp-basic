package next.service;

import next.CannotDeleteException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
    @Mock
    private QuestionDao questionDao;

    @Mock
    private AnswerDao answerDao;

    private QnaService qnaService;

    @Before
    public void setup() {
        qnaService = new QnaService(questionDao, answerDao);
    }

    @Test(expected = CannotDeleteException.class)
    public void 없는_질문_삭제_테스트() throws Exception {
        when(questionDao.findById(1L)).thenReturn(null);

        qnaService.deleteQuestion(1L, newUser("userId"));
    }

    @Test
    public void 질문_삭제_테스트() throws Exception {
        User user = newUser("userId");

        Question question = new Question(1L, user.getUserId(), "title", "contents", new Date(), 0) {
            public boolean canDelete(User user, List<Answer> answers) throws CannotDeleteException {
                return true;
            };
        };

        when(questionDao.findById(1L)).thenReturn(question);

        qnaService.deleteQuestion(1L, newUser("userId"));
        verify(questionDao).delete(question.getQuestionId());
    }

    @Test(expected = CannotDeleteException.class)
    public void 질문_삭제_불가능_테스트() throws Exception {
        User user = newUser("userId");

        Question question = new Question(1L, user.getUserId(), "title", "contents", new Date(), 0) {
            public boolean canDelete(User user, List<Answer> answers) throws CannotDeleteException {
                throw new CannotDeleteException("삭제할 수 없음");
            };
        };

        when(questionDao.findById(1L)).thenReturn(question);

        qnaService.deleteQuestion(1L, newUser("userId"));
    }

    public static User newUser(String userId) {
        return new User(
                userId,
                "password",
                "name",
                "test@sample.com"
        );
    }

}