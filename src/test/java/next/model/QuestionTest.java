package next.model;

import next.CannotDeleteException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class QuestionTest {
    public static User newUser(String userId) {
        return new User(
                userId,
                "password",
                "name",
                "test@sample.com"
        );
    }

    public static Question newQuestion(String writer) {
        return new Question(
                1L,
                writer,
                "title",
                "contents",
                new Date(),
                0
        );
    }

    public static Question newQuestion(long questionId, String writer) {
        return new Question(
                questionId,
                writer,
                "title",
                "contents",
                new Date(),
                0
        );
    }

    public static Answer newAnswer(String writer) {
        return new Answer(writer, "contents", 1L);
    }

    @Test(expected = CannotDeleteException.class)
    public void 글쓴이_다른_게시물_삭제() throws Exception {
        User user = newUser("kolon");
        Question question = newQuestion("samsung");

        question.canDelete(user, new ArrayList<>());
    }

    @Test
    public void 글쓴이는_같고_답변_없는_게시물_삭제() throws Exception {
        User user = newUser("kolon");
        Question question = newQuestion("kolon");

        assertTrue(question.canDelete(user, new ArrayList<>()));
    }

    @Test
    public void 글쓴이는_같고_같은_글쓴이_답변_게시물_삭제() throws Exception {
        User user = newUser("kolon");
        Question question = newQuestion("kolon");
        List<Answer> answers = Arrays.asList(newAnswer("kolon"));

        assertTrue(question.canDelete(user, answers));
    }

    @Test(expected = CannotDeleteException.class)
    public void 글쓴이는_같고_다른_글쓴이_답변_게시물_삭제() throws Exception {
        User user = newUser("kolon");
        Question question = newQuestion("kolon");
        List<Answer> answers = Arrays.asList(newAnswer("samsung"));

        question.canDelete(user, answers);
    }

}