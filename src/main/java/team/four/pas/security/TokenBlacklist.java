package team.four.pas.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TokenBlacklist {

    private Integer expirationTimeMillis;

    private Cache<String, Boolean> blackList = Caffeine.newBuilder()
                                                        .expireAfterWrite(expirationTimeMillis,
                                                                TimeUnit.MILLISECONDS)
                                                       .build();
    public void add(String token){
        blackList.put(token, true);
    }

    public boolean contains(String token){
        return Boolean.TRUE.equals(blackList.getIfPresent(token));
    }
}
