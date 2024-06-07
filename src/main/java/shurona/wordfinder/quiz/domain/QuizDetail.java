package shurona.wordfinder.quiz.domain;

import jakarta.persistence.*;
import shurona.wordfinder.common.DateInfoEntity;
import shurona.wordfinder.word.domain.JoinWordUser;
import shurona.wordfinder.word.domain.Word;

@Entity
public class QuizDetail extends DateInfoEntity {
    @Id
    @GeneratedValue
    @Column(name = "QUIZ_DETAIL_ID")
    private Long id;

    private int sequence;

    private boolean isCorrect;

    private String receiveAnswer;

    private int answerLoc;

    private String firstMeaning;

    private String secondMeaning;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUIZ_SET_ID")
    private QuizSet quizSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORD_USER_ID")
    private JoinWordUser joinWordUser;

    protected QuizDetail() {
    }

    /*
    Getter
     */

    public Long getId() {
        return id;
    }

    public int getSequence() {
        return sequence;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public String getReceiveAnswer() {
        return receiveAnswer;
    }

    public JoinWordUser getJoinWordUser() {
        return this.joinWordUser;
    }

    public int getAnswerLoc() {
        return answerLoc;
    }

    public String getFirstMeaning() {
        return firstMeaning;
    }

    public String getSecondMeaning() {
        return secondMeaning;
    }

    /*
     First Time Setter
     */

    private void setFirstDetail(int sequence, int answerLoc, boolean isCorrect, JoinWordUser wordUser, String firstMeaning, String secondMeaning) {
        this.sequence = sequence;
        this.answerLoc = answerLoc;
        this.joinWordUser = wordUser;
        this.isCorrect = isCorrect;
        this.firstMeaning = firstMeaning;
        this.secondMeaning = secondMeaning;
    }

    public void setQuizSet(QuizSet quizSet) {
        this.quizSet = quizSet;
    }

    /*
        퀴즈 디테일 생성
     */
    public static QuizDetail createQuizDetail(
            int sequence,
            int answerLoc,
            JoinWordUser wordUser,
            String firstMeaning,
            String secondMeaning
            ) {

        QuizDetail detail = new QuizDetail();
        detail.setDataAtFirstTime();
        detail.setFirstDetail(sequence, answerLoc, false, wordUser, firstMeaning, secondMeaning);

        return detail;
    }

    // ============================== 도메인 로직 ==============================
    public void goodAnswer() {
        this.isCorrect = true;
    }


    /*
     테스트용
     */

    @Override
    public String toString() {
        return "QuizDetail{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", isCorrect=" + isCorrect +
                ", receiveAnswer='" + receiveAnswer + '\'' +
                '}';
    }
}
