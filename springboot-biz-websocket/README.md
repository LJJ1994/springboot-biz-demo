在 Spring Boot 中使用 WebSocket，您可以使用 Spring 的 `spring-boot-starter-websocket` 模块。以下是一个简单的 WebSocket 示例，包括服务端和客户端。

### 1. 创建 Spring Boot 项目

首先，创建一个新的 Spring Boot 项目，添加以下依赖到您的 `pom.xml` 文件中：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
</dependencies>
```

### 2. 编写 WebSocket 服务端

创建一个 WebSocket 处理器类，处理连接和消息：

```java
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established with session id: " + session.getId());
        session.sendMessage(new TextMessage("Connection established. Welcome!"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        session.sendMessage(new TextMessage("Received your message: " + message.getPayload()));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Error in WebSocket connection: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Connection closed with session id: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
```

### 3. 配置 WebSocket

创建配置类`WebSocketConfig`，添加 `@EnableWebSocket` 注解：

```java
import com.hellwalker.biz.websocket.handler.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/my-websocket").setAllowedOrigins("*");
    }
}
```

在上述代码中，`@EnableWebSocket` 启用 WebSocket 支持，并通过 `WebSocketConfigurer` 接口实现了 WebSocket 的配置。`MyWebSocketHandler` 处理了 WebSocket 连接和消息。

### 4. 编写 WebSocket 客户端

您可以使用浏览器的 JavaScript 或者其他 WebSocket 客户端来测试 WebSocket 连接。以下是一个简单的 JavaScript 客户端的例子：

```html
<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Demo</title>
</head>
<body>
    <h1>WebSocket Demo</h1>
    <div id="messages"></div>

    <script>
        const socket = new WebSocket("ws://localhost:8080/my-websocket");

        socket.onopen = function(event) {
            console.log("WebSocket connection opened.");
            sendMessage("Hello, WebSocket!");
        };

        socket.onmessage = function(event) {
            console.log("Received message: " + event.data);
            displayMessage(event.data);
        };

        socket.onclose = function(event) {
            console.log("WebSocket connection closed.");
        };

        function sendMessage(message) {
            socket.send(message);
            console.log("Sent message: " + message);
        }

        function displayMessage(message) {
            const messagesDiv = document.getElementById("messages");
            messagesDiv.innerHTML += "<p>" + message + "</p>";
        }
    </script>
</body>
</html>
```

### 5. 运行应用

运行您的 Spring Boot 应用，然后访问 `http://localhost:8080`，打开开发者工具查看浏览器的控制台，您将看到 WebSocket 连接的日志。