package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import sample.entity.OutputMessage;
import sample.repository.MessageRepository;


@Controller
public class ChatController {
    @Autowired
    MessageRepository messageRepository;

    @MessageMapping("/chat/{sessionId}")
    @SendTo("/topic/messages")
    public OutputMessage send(@DestinationVariable("sessionId") String sessionId,
                              InputMessage inputMessage) throws Exception {
        OutputMessage message = new OutputMessage(inputMessage.getFrom(), inputMessage.getText());
        messageRepository.save(message);
        return message;
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
