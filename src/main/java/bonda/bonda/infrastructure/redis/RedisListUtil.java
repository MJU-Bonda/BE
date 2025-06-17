package bonda.bonda.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class RedisListUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public void addToRecentList(String key, String value, int maxSize) {
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();
        listOperations.remove(key, 0, value);           // 중복 제거
        listOperations.leftPush(key, value);                  // 맨 앞에 삽입
        listOperations.trim(key, 0, maxSize - 1);   // LRU 유지
    }

    public List<String> getList(String key) {
        List<String> result = stringRedisTemplate.opsForList().range(key, 0, -1);
        return result != null ? result : Collections.emptyList();
    }

    public void removeItem(String key, String value) {
        stringRedisTemplate.opsForList().remove(key, 1,  value);
    }

    public void deleteList(String key) {
        stringRedisTemplate.delete(key);
    }
}
