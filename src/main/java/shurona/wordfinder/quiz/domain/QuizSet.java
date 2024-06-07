package shurona.wordfinder.quiz.domain;

import jakarta.persistence.*;
import shurona.wordfinder.common.DateInfoEntity;
import shurona.wordfinder.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class QuizSet extends DateInfoEntity {
    @Id
    @GeneratedValue
    @Column(name = "QUIZ_SET_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    private int currentSequence;

    @OneToMany(mappedBy = "quizSet", cascade = CascadeType.ALL)
    private List<QuizDetail> quizDetails = new ArrayList<>();

    protected QuizSet() {
    }

    private void setUserFirstTime(User user) {
        this.user = user;
    }


    /**
     * 메서드 저장방식
     */
    private void addDetail(QuizDetail quizDetail) {
        this.quizDetails.add(quizDetail);
        quizDetail.setQuizSet(this);
    }

    /*
     Getter
     */

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getCurrentSequence() {
        return currentSequence;
    }

    public List<QuizDetail> getQuizDetails() {
        return quizDetails;
    }



    /*
        생성 메서드
     */
    public static QuizSet createQuizSet(User user, QuizDetail[] details) {
        QuizSet quizSet = new QuizSet();

        quizSet.setDataAtFirstTime();
        quizSet.setUserFirstTime(user);

        for (QuizDetail detail : details) {
            quizSet.addDetail(detail);
        }

        return quizSet;
    }

    /*
    =========================== 도메인 로직 =============================
     */

    public int increaseQuizSequence() {
        // 현재 최대치면 패스
        int MAX_QUIZ_LENGTH = 9;
        if (this.currentSequence >= MAX_QUIZ_LENGTH) {
            this.currentSequence = MAX_QUIZ_LENGTH + 1;
            return -1;
        }
        this.currentSequence += 1;

        return this.currentSequence;
    }
}
