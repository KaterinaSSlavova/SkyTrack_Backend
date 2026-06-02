package skytrack;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;
import skytrack.presentation.security.LoginRateLimiter;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRateLimiterTest {

    private final LoginRateLimiter rateLimiter = new LoginRateLimiter();

    @Test
    void resolveBucket_shouldReturnBucket() {
        Bucket bucket = rateLimiter.resolveBucket("192.168.1.1");
        assertThat(bucket).isNotNull();
    }

    @Test
    void resolveBucket_samIp_shouldReturnSameBucket() {
        Bucket first = rateLimiter.resolveBucket("192.168.1.1");
        Bucket second = rateLimiter.resolveBucket("192.168.1.1");
        assertThat(first).isSameAs(second);
    }

    @Test
    void resolveBucket_differentIps_shouldReturnDifferentBuckets() {
        Bucket first = rateLimiter.resolveBucket("192.168.1.1");
        Bucket second = rateLimiter.resolveBucket("192.168.1.2");
        assertThat(first).isNotSameAs(second);
    }

    @Test
    void resolveBucket_shouldAllow5Requests() {
        Bucket bucket = rateLimiter.resolveBucket("10.0.0.1");
        for (int i = 0; i < 5; i++) {
            assertThat(bucket.tryConsume(1)).isTrue();
        }
    }

    @Test
    void resolveBucket_shouldBlockAfter5Requests() {
        Bucket bucket = rateLimiter.resolveBucket("10.0.0.2");
        for (int i = 0; i < 5; i++) {
            bucket.tryConsume(1);
        }
        assertThat(bucket.tryConsume(1)).isFalse();
    }
}