package backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // WebSocketメッセージブローカーを有効にする
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // "/topic" から始まる宛先のメッセージをブローカーが処理するよう設定
        // クライアントはこの宛先を購読(subscribe)する
        config.enableSimpleBroker("/topic");
        // "/app" から始まる宛先のメッセージは、@MessageMappingアノテーションを付けたメソッドが処理する
        // (今回はクライアントからのメッセージ送信はないため、主にバックエンドからのプッシュで使用)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocketの接続エンドポイントを "/ws" として登録
        // SockJSを有効にすることで、WebSocketが使えない環境でも代替技術で接続可能になる
        registry.addEndpoint("/ws").withSockJS();
    }
}