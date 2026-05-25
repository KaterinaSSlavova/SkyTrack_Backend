package skytrack.config;

import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String cookieHeader = request.getHeaders().getFirst("cookie");
        if (cookieHeader == null) {
            return true;
        }

        String token = Arrays.stream(cookieHeader.split(";"))
                .map(String::trim)
                .filter(cookie -> cookie.startsWith("jwt="))
                .map(cookie -> cookie.substring(4))
                .findFirst()
                .orElse(null);

        attributes.put("jwt", token);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {}
}
