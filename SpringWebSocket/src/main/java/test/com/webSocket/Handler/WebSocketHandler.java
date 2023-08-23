package test.com.webSocket.Handler;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import test.com.webSocket.Model.ChatMessage;
import test.com.webSocket.Model.ChatRoom;
import test.com.webSocket.Service.ChatService;

@Slf4j
@RequiredArgsConstructor
@Component
@Lazy
public class WebSocketHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper;
//	Object ���۴� ������ �޾�  joson �����ͷ� ��ȯ
	private final ChatService chatService;
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("{}", payload);
		ChatMessage chatmessage = objectMapper.readValue(payload,ChatMessage.class);
		
		
		 ChatRoom chatRoom = chatService.findRoomById(chatmessage.getRoomId());
	        chatRoom.HandlerActions(session, chatmessage, chatService);
	}
	
// ������ �����ϰ� �Ѵٸ�  �޽����� json�� ���·� �������� ���ؼ� ������ ������, Handler�� �̸� �޾� ObjectMapper�� ���ؼ� json �����͸� chatMessage.clss�� �°� �Ľ�
// ChatMessgae ��ü�� ��ȯ�� ��Ŵ / �� json �����Ϳ� ����ִ� roomId�� �̿��ؼ� �ش� ä�ù��� ã�� HandlerAction�̶�� �޼��带 �����ų����.
}
