package test.com.webSocket.Model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK
    }
//    enum�� ���ؼ� ������ ��(Enter), ä���� ������ ��(Talk)���� Ÿ���� ����

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}