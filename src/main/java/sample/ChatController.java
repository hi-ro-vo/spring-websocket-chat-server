package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import sample.entity.OutputMessage;
import sample.entity.User;
import sample.repository.MessageRepository;


@Controller
public class ChatController {

    @Autowired
    SimpUserRegistry userRegistry;
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

    @MessageMapping("/chat/users/{sessionId}")
    @SendTo("/topic/users/{sessionId}")
    public OutputMessage sendUsers(@DestinationVariable("sessionId") String sessionId) throws Exception {
        String users = "";
        for (SimpUser u: userRegistry.getUsers()) {
            users = users+" "+u.getName();
        }
        return new OutputMessage("System", users);
    }
    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
