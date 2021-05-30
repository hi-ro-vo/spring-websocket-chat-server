package sample;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.DestinationVariable;

/*
 * Chat Controller listens for chat topic and responds with a message.
 *
 * @Author Jay Sridhar
 */
@Controller
public class ChatController 
{
    @MessageMapping("/chat/{topic}")
    @SendTo("/topic/messages")
    public OutputMessage send(@DestinationVariable("topic") String topic,
			      InputMessage inputMessage) throws Exception
    {
	return new OutputMessage(inputMessage.getFrom(), inputMessage.getText(), topic);
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
