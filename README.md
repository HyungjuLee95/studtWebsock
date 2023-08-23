# studyWebsock
studtWebsock


## WebSocket이란 무엇인가?
WebBrowser에서 Request를 보낸다면 Server는 Response를 준다. Http 통신의 기본적인 동작 방식이다.<br>
생각해본다면 Server에서 client로 특정 행위를 알려야하는 상황이 있다. 예를 들어서 알림, 혹은 친구로부터 온 쪽지 등이다.<br>
공부한 바로는 이전에는 LongPoliing을 통하여 해결했었으나, WebSocket을 통해 ServerClient 간의 실시간 통신이 가능하게 되었으며, WebSocket이 더욱 용이하다는 판단으로 공부를 시작한다.

## HandShake
흔히 악수라고 한다.
WebSocket은 Http 기반으로 HandShaking을 한다. 어떠한 방식일까?

### Handshake 요청
```
GET /chat HTTP/1.1
Host: server.example.com
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==
Origin: http://example.com
Sec-WebSocket-Protocol: v10.stomp, v11.stomp, my-team-custom
Sec-WebSocket-Version: 13
```
1) Connection: Upgrade : Http 사용 방식을 변경하자.
2) Upgrade: websocket : WebSocket을 이용
3) Sec-WebSocket-Protocol: 111,222,333 : Websocket을 쓰면서 이중에서(111,222,333) protocol을 골라서 쓰자.
4) Sec-WebSocket-Version: 13 : 보안을 위한 요청 키.

### HandShake 응답
   
```
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
```
1) 101 Switching Protocols : handshake 요청 내용을 기반으로 다음부터 WebSocket으로 통신 가능
2) Sec-WebSocket-Accept : 보안을 위한 응답 키 


### WebSocket Server를 운용할 때의 유의사항
1) http에서 동작하나, 방식이 http와는 많이 상이하다.
   -> REST 방식의 http 통신에서는 많은 URI를 통해 Application이 설계됨
   -> WebSocket은 하나의 URL을 통해 Connection이 맺어지고, 후에는 해당 Connection으로만 통신한다.  
2) handshake가 완료되고 Connection을 유지한다 .
   ->  흔히 알고있는 JTTP 통신은 요청과 응답이 완료되면 Connection이 close가 된다. 때문에 이론상 하나의 Server가 Port수의 한계를 넘는 Client의 요청을 처리 불가하다.
   -> Websocket은 Connection을 유지하고 있으므로, 가용 Port 수만큼의 Client와 통신 가능
---

### 이용할 인터페이스
#### *WebSocketConfigurer 

   void registerWebSocketHandlers(WebSocketHandlerRegistry registry):<br>
이 메서드는 WebSocket 핸들러를 등록하는 데 사용됩니다. WebSocket 핸들러는 클라이언트와 서버 간의 실제 데이터 통신을 처리하는 역할.<br>
WebSocketHandlerRegistry 객체를 통해 어떤 URL 패턴에 대해 어떤 핸들러를 사용할 것인지 등록 가능.<br><br>
![image](https://github.com/HyungjuLee95/studyWebsock/assets/111270174/de390110-c3ac-4fc7-8dda-99f79f8a08c0)
> websockethandler 클래스를 직접 지정 <br> <br>
void addInterceptors(WebSocketHandlerRegistry registry):<br>
WebSocket 연결을 가로채거나 조작하기 위해 인터셉터를 추가하는 데 사용됩니다. 인터셉터는 연결이나 메시지 전송 등의 이벤트를 감지하고 처리하는데 사용.<br><br>

void configureWebSocketTransport(WebSocketTransportRegistration registry):<br>
WebSocket 전송 옵션을 구성하는 데 사용됩니다. 이 메서드를 통해 WebSocket 전송과 관련된 설정을 조정할 수 있습니다. 예를 들어, 버퍼 크기, 메시지 크기 제한 등을 설정 가능.<br><br>

void configureWebSocketTransport(WebSocketTransportRegistration registry):<br>
WebSocket 전송 옵션을 구성하는 데 사용됩니다. 이 메서드를 통해 WebSocket 전송과 관련된 설정을 조정할 수 있습니다. 예를 들어, 버퍼 크기, 메시지 크기 제한 등을 설정 가능.<br><br>

default void configureWebSocketTransport(WebSocketTransportRegistration registry):<br>
WebSocket 전송 옵션을 구성하는 데 사용되는 디폴트 메서드입니다. 원하는 경우 이 메서드를 오버라이드하여 WebSocket 전송 설정을 추가 또는 수정 가능.<br><br>


#### **TextWebSocketHandler  
void afterConnectionEstablished(WebSocketSession session):<br>
클라이언트와 WebSocket 연결이 성립되었을 때 호출됩니다. 이 메서드를 사용하여 연결 초기화 작업을 수행할 수 있음.<br>
예를 들어, 클라이언트가 연결되었다는 메시지를 로깅하거나 초기 데이터 전송 가능.<br><br>

void handleTextMessage(WebSocketSession session, TextMessage message):<br>
클라이언트로부터 텍스트 기반의 메시지가 도착했을 때 호출됩니다. 이 메서드를 사용하여 수신된 메시지를 처리하고, 필요에 따라 응답을 생성하거나 로직을 수행 가능.<br><br>

void handleTransportError(WebSocketSession session, Throwable exception):<br>
WebSocket 연결 중에 에러가 발생했을 때 호출됨. 연결 에러를 처리하고 필요한 조치를 취할 수 있음.<br><br>

void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus):<br>
WebSocket 연결이 닫혔을 때 호출됩니다. 연결이 닫힌 후 정리 작업을 수행할 수 있음. <br>
CloseStatus 객체는 연결이 닫힌 원인 및 상태에 대한 정보를 제공.<br><br>

---

## Stomp 이용하기
### Stomp
기존 이용하는 방식은 Application에서 직접 Session을 처리하였는데, Stomp의 경우 proxy에 가까운 역하을 하게 된다<br><br>
*Proxy : 다른 객체에 대한 대리자나 대변 역할을 수행하는 중간 개체, 원본 객체와 클라이어트 사이에서 동작하여 특정 작업을 제어하거나 보안 로깅, 서능 최적화 등의 추가 기능을 제공한다.<br><br>
생각해보자 WebSocketHandler의 경우에는 message 타입이 binary 혹은 text로 나뉜다고 했다. message가 binary거나 text이기만 하면 실제 내용물이 무엇인지와는 상관없이 통신이 이루어지게된다.<br>
