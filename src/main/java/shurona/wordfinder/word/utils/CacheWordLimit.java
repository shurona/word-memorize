package shurona.wordfinder.word.utils;

public interface CacheWordLimit {
    /**
     * 단어 하나 저장 시 남은 count 반환
     */
    public int useWordCount(Long userId);

    /**
     * 현재 남은 count를 확인해 본다.
     */
    public int checkCount(Long userId);

    /**
     * 필요에 따라서 카운트를 1 증가시킨다.
     */
    public int rollBackCount(Long userId);
}
