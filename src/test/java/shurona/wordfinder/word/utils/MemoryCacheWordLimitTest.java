package shurona.wordfinder.word.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MemoryCacheWordLimitTest {

    MemoryCacheWordLimit memoryCacheWordLimit = new MemoryCacheWordLimit();

    final int currentDailyMaxCount = 12;

    @AfterEach
    public void afterEach() {
        this.memoryCacheWordLimit.cacheClear();
    }

    @Test
    void 처음_생성() {

        // given
        Long userId = 1L;

        // when
        int count = this.memoryCacheWordLimit.checkCount(userId);

        // then
        assertThat(count).isEqualTo(this.currentDailyMaxCount);
    }

    @Test
    void 생성후_카운트_증가() {

        // given
        Long userId = 1L;
        Long userNone = 2L;
        int loopCount = 5;
        this.memoryCacheWordLimit.checkCount(userId);

        // when
        int currentCount = 0;
        for (int i = 0; i < loopCount; i++) {
            currentCount = this.memoryCacheWordLimit.useWordCount(userId);
        }
        int i = this.memoryCacheWordLimit.useWordCount(userNone);

        // then
        assertThat(currentCount).isEqualTo(this.currentDailyMaxCount  - loopCount);
        assertThat(i).isEqualTo(-1);

    }

}