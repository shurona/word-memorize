package shurona.wordfinder.quiz.dto;

import java.util.ArrayList;
import java.util.List;

public class QuizAnswerForm {

    private Long quizSetId;
    private String word;
    private int sequence;

    private Integer selectedAnswer;

    private List<String> meaningList = new ArrayList<>();

    public Long getQuizSetId() {
        return quizSetId;
    }

    public void setQuizSetId(Long quizSetId) {
        this.quizSetId = quizSetId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Integer getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(Integer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public List<String> getMeaningList() {
        return meaningList;
    }

    public void setMeaningList(List<String> meaningList) {
        this.meaningList = meaningList;
    }

    @Override
    public String toString() {
        return "QuizAnswerForm{" +
                "word='" + word + '\'' +
                ", sequence=" + sequence +
                ", selectedAnswer=" + selectedAnswer +
                ", meaningList=" + meaningList +
                '}';
    }
}
