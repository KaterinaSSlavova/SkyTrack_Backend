package skytrack.presentation.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ip){
        return buckets.computeIfAbsent(ip, this::newBucket);
    }

    private Bucket newBucket(String ip){
        return Bucket.builder().addLimit(
                Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))).build();
    }
}