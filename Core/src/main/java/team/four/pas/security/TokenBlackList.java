package team.four.pas.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenBlackList {

    private Cache<String, Boolean> blackList;

    public TokenBlackList(@Value("${jwt.time}") Integer timeout) {
        blackList = Caffeine.newBuilder()
                            .expireAfterWrite(timeout, TimeUnit.MILLISECONDS)
                            .build();
    }

    public void add(String token){
        blackList.put(token, true);
    }

    public boolean contains(String token){
        return Boolean.TRUE.equals(blackList.getIfPresent(token));
    }
}
