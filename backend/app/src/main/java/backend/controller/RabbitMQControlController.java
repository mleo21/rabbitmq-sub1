package backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RabbitMQControlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQControlController.class);

    private final RabbitListenerEndpointRegistry listenerRegistry;
    private final String LISTENER_ID = "my-queue-listener";

    public RabbitMQControlController(RabbitListenerEndpointRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    /**
     * フロントエンドから "/app/control/start" という宛先にメッセージが送られたときに呼び出される。
     */
    @MessageMapping("/control/start")
    public void startListener() {
        LOGGER.info("Received request to START listener.");
        listenerRegistry.getListenerContainer(LISTENER_ID).start();
    }

    /**
     * フロントエンドから "/app/control/stop" という宛先にメッセージが送られたときに呼び出される。
     */
    @MessageMapping("/control/stop")
    public void stopListener() {
        LOGGER.info("Received request to STOP listener.");
        listenerRegistry.getListenerContainer(LISTENER_ID).stop();
    }
}
