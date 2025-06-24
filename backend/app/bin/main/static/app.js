// src/main/resources/static/app.js

const { createApp, ref, computed } = Vue;

createApp({
    setup() {
        const messages = ref([]);
        const isConnected = ref(false);
        let stompClient = null;

        const connectionStatus = computed(() => {
            return isConnected.value
                ? { text: '● 受信中', class: 'connected' }
                : { text: '○ 切断', class: 'disconnected' };
        });

        const connect = () => {
            if (isConnected.value) return;
            console.log('Connecting...');
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect(
                {},
                (frame) => {
                    console.log('Connected: ' + frame);
                    isConnected.value = true;
                    
                    // ▼▼▼ ここから追加 ▼▼▼
                    // バックエンドにリスナー開始を通知
                    stompClient.send('/app/control/start', {}, '');
                    console.log('Sent start command to backend.');
                    // ▲▲▲ ここまで追加 ▲▲▲

                    stompClient.subscribe('/topic/messages', (message) => {
                        const receivedMessage = JSON.parse(message.body);
                        messages.value.unshift(receivedMessage);
                    });
                },
                (error) => {
                    console.error('Connection error: ' + error);
                    isConnected.value = false;
                    stompClient = null;
                    alert('サーバーへの接続に失敗しました。');
                }
            );
        };

        const disconnect = () => {
            if (stompClient !== null && isConnected.value) {
                // ▼▼▼ ここから追加 ▼▼▼
                // バックエンドにリスナー停止を通知
                stompClient.send('/app/control/stop', {}, '');
                console.log('Sent stop command to backend.');
                // ▲▲▲ ここまで追加 ▲▲▲

                stompClient.disconnect(() => {
                    console.log('Disconnected');
                    isConnected.value = false;
                    stompClient = null;
                    messages.value = [];
                });
            }
        };

        return {
            messages,
            isConnected,
            connectionStatus,
            connect,
            disconnect
        };
    }
}).mount('#app');