package sample;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.DestinationVariable;


@Controller
public class ChatController 
{
    @MessageMapping("/chat/{sessionId}")
    @SendTo("/topic/messages")
    public OutputMessage send(@DestinationVariable("sessionId") String sessionId,
			      InputMessage inputMessage) throws Exception
    {
	return new OutputMessage(inputMessage.getFrom(), inputMessage.getText());
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
