package shurona.wordfinder.quiz.dto;

import java.time.LocalDateTime;

public class QuizListForm {
    private Long id;
    private Integer process;

    private LocalDateTime dateTime;

    private boolean isEnd;


    public QuizListForm(Long id, Integer process, LocalDateTime dateTime, boolean isEnd) {
        this.id = id;
        this.process = process;
        this.dateTime = dateTime;
        this.isEnd = isEnd;
    }

    public Long getId() {
        return id;
    }

    public Integer getProcess() {
        return process;
    }

    public void setProcess(Integer process) {
        this.process = process;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "QuizListForm{" +
                "id=" + id +
                ", process=" + process +
                ", dateTime=" + dateTime +
                ", isEnd=" + isEnd +
                '}';
    }
}
