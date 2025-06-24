package backend.service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.stereotype.Service;

// @Service
// public class RabbitMQConsumer {

//     private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

//     /**
//      * 指定されたキューからメッセージを受信するリスナーメソッド。
//      *
//      * @RabbitListener アノテーション：
//      * - このメソッドがRabbitMQのリスナーであることを示します。
//      * - queuesプロパティで監視対象のキュー名を指定します。
//      * - '${myapp.rabbitmq.queue-name}' のように書くことで、application.propertiesの値を使用できます。
//      *
//      * 注意：このキューはRabbitMQサーバーに事前に存在している必要があります。
//      * 存在しない場合はエラーになるため、後述の「キューの自動作成」が推奨されます。
//      */
//     @RabbitListener(queues = {"${myapp.rabbitmq.queue-name}"})
//     public void consume(String message) {
//         LOGGER.info(String.format("Received message -> %s", message));
//         // ここで受信したメッセージに対する処理を実装します。
//         // (例: データベースへの保存、別のサービスへの通知など)
//     }
// }


import backend.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    // WebSocket経由でメッセージを送信するためのヘルパークラス
    private final SimpMessagingTemplate messagingTemplate;

    // コンストラクタでSimpMessagingTemplateをインジェクションする
    public RabbitMQConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(id = "my-queue-listener", queues = {"${myapp.rabbitmq.queue-name}"}, autoStartup = "false")
    public void consume(String rabbitMessage) {
        LOGGER.info(String.format("Received message from RabbitMQ -> %s", rabbitMessage));

        // 受信した内容をMessageオブジェクトに変換
        Message webSocketMessage = new Message(rabbitMessage);

        // WebSocketの宛先「/topic/messages」にメッセージを送信
        // この宛先を購読している全クライアントにメッセージが配信される
        messagingTemplate.convertAndSend("/topic/messages", webSocketMessage);
    }
}