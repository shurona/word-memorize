package shurona.wordfinder.word.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * TODO: 추후 Redis로 이관시킨다.
 */
@Component
public class MemoryCacheWordLimit implements CacheWordLimit{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final HashMap<String, CountWithDate> cacheStore = new HashMap<>();

    private final String KEY_PREFIX = "user-";
    private final int DAILY_MAX_COUNT = 12;

    // 매일 폴링으로 제거
    @Scheduled(cron = "0 0 0 * * *")
    public void run() {
        this.log.info("Reset Today word cache length : {}", cacheStore.size());
        cacheStore.clear();
    }


    private void saveWordCount(Long userId) {
        cacheStore.put(this.KEY_PREFIX + userId, new CountWithDate());
    }

    @Override
    public int checkCount(Long userId) {
        CountWithDate countWithDate = cacheStore.getOrDefault(this.KEY_PREFIX + userId, null);
        if (countWithDate == null) {
            this.saveWordCount(userId);
            countWithDate = cacheStore.get(this.KEY_PREFIX + userId);
        }
        return this.DAILY_MAX_COUNT - countWithDate.getCount();
    }

    @Override
    public int useWordCount(Long userId) {

        CountWithDate countDate = cacheStore.getOrDefault(this.KEY_PREFIX + userId, null);
        if (countDate == null) {
            return -1;
        }
        countDate.increaseCount();
        return this.DAILY_MAX_COUNT - countDate.getCount();
    }

    public int rollBackCount(Long userId) {
        CountWithDate countDate = cacheStore.getOrDefault(this.KEY_PREFIX + userId, null);
        if (countDate == null) {
            return -1;
        }
        countDate.decreaseCount();
        return this.DAILY_MAX_COUNT - countDate.getCount();
    }

    private static class CountWithDate {
        private LocalDateTime createdTime;
        private  int count;

        public LocalDateTime getCreatedTime() {
            return createdTime;
        }

        public int getCount() {
            return count;
        }

        public CountWithDate() {
            this.createdTime = LocalDateTime.now();
            this.count = 0;
        }

        public void increaseCount() {
            this.count += 1;
        }

        public void decreaseCount() {
            this.count -= 1;
        }
    }

    // 테스트에서만 사용
    public void cacheClear() {
        cacheStore.clear();
    }
}
