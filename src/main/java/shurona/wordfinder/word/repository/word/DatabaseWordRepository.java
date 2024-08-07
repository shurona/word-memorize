package shurona.wordfinder.word.repository.word;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import shurona.wordfinder.word.domain.Word;
import shurona.wordfinder.word.repository.word.repodto.RandWordMeaningDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class DatabaseWordRepository implements WordRepository {
    @PersistenceContext
    EntityManager em;

    @Override
    public String save(Word word) {
        this.em.persist(word);
        return word.getUid();
    }

    @Override
    public Word findWordByWord(String word) {
        String query = "select word from Word as word where word.word = :word";

        List<Word> resultList = this.em.createQuery(query, Word.class)
                .setParameter("word", word)
                .getResultList();

        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    @Override
    public Optional<Word> findWordById(String id) {
        Word word = this.em.find(Word.class, id);
        return Optional.ofNullable(word);
    }

    @Override
    public Word[] findWordsByIds(String[] ids) {
        String query = "select word from Word as word where word.uid in :ids";
        List<Word> resultList = this.em.createQuery(query, Word.class)
                .setParameter("ids", Arrays.asList(ids))
                .getResultList();

        return resultList.toArray(Word[]::new);
    }

    @Override
    public List<RandWordMeaningDto> findRandomWordMeaning(String exceptWordId) {
        String query = "select new shurona.wordfinder.word.repository.word.repodto.RandWordMeaningDto(w.uid, w.meaning) " +
                "from Word w " +
                "where w.id in " +
                "(select jwu.word.id from JoinWordUser as jwu where jwu.visible = true order by random() limit 10)";
        List<RandWordMeaningDto> resultList = this.em.createQuery(query, RandWordMeaningDto.class).getResultList();


        List<RandWordMeaningDto> output = new ArrayList<>();
        int count = 0;
        for (RandWordMeaningDto randWordMeaningDto : resultList) {
            if (count == 3) {
                break;
            }
            // 답으로 선정된 뜻은 제외되어야 한다.
            if (randWordMeaningDto.getWordId().equals(exceptWordId)) {
                continue;
            }
            output.add(randWordMeaningDto);
            count += 1;
        }
        return output;
    }

    @Override
    public void editMeaning(String id, String meaning) {
        String query = "update Word as word set word.meaning = :meaning, word.status = 'COMPLETE'" +
                " where word.uid = :id";
        this.em.createQuery(query)
            .setParameter("meaning", meaning)
            .setParameter("id", id)
            .executeUpdate();
        this.em.clear();
    }
}
