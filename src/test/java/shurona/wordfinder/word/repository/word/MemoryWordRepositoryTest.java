package shurona.wordfinder.word.repository.word;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import shurona.wordfinder.word.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemoryWordRepositoryTest {

    WordRepository wordRepository = new MemoryWordRepository();

    @Test
    void save() {
        // given
        // 한 개를 일단 저장한다.
        Word word = new Word();

        UUID uid = UUID.randomUUID();

        word.setWord("wordOne");
        word.setUid(uid);

        // when
        Word output = this.wordRepository.save(word);

        // then
        // 저장에서 갖고 온 값 하고 넣어준 값이 같은 지 확인한다.
        assertThat(output).isEqualTo(word);
    }

    @Test
    void findWordByWord() {
        // given
        // 저장을 먼저 한다.
        ArrayList<UUID> uuidArray = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Word word = new Word();
            word.setWord("Word" + i);

            Word afterSaved = this.wordRepository.save(word);
            uuidArray.add(afterSaved.getUid());

        }

        int wishNumber = 30;
        // when
        Word foundWord = this.wordRepository.findWordByWord("Word" + wishNumber);

        // then
        assertThat(foundWord.getWord()).isEqualTo("Word" + wishNumber);
        assertThat(uuidArray.get(wishNumber - 1)).isEqualTo(foundWord.getUid());
    }

    @Test
    void findWordByWordIfNotExist() {
        // given
        // 저장을 먼저 한다.
        for (int i = 1; i <= 100; i++) {
            Word word = new Word();
            word.setWord("Word" + i);

            Word afterSaved = this.wordRepository.save(word);
        }

        // when
        int wishNumber = 1000;
        Word foundWord = this.wordRepository.findWordByWord("Word" + wishNumber);

        // then
        assertThat(foundWord).isNull();
    }

    @Test
    void findWordsByIds() {
        // given
        // 저장을 먼저 한다.
        ArrayList<UUID> uuidArray = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Word word = new Word();
            word.setWord("Word" + i);

            Word afterSaved = this.wordRepository.save(word);
            uuidArray.add(afterSaved.getUid());
        }

        // when
        List<UUID> uuids = uuidArray.subList(0, 10);
        Word[] wordList = this.wordRepository.findWordsByIds(uuids.toArray(UUID[]::new));

        // then
        for (int i = 0; i < 10; i++) {
            assertThat(wordList[i].getWord()).isEqualTo("Word" + (i + 1));
        }
    }
}