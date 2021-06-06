package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private static final String USERNAME_HEADER = "login";
    private static final String PASSWORD_HEADER = "pass";
    private static final String REGISTER_HEADER = "register";
    private final WebSocketAuthenticatorService webSocketAuthenticatorService;

    @Autowired
    @Qualifier("clientOutboundChannel")
    private MessageChannel clientOutboundChannel;

    @Autowired
    WebSocketRegisterService registerService;

    @Autowired
    public AuthChannelInterceptorAdapter(final WebSocketAuthenticatorService webSocketAuthenticatorService) {
        this.webSocketAuthenticatorService = webSocketAuthenticatorService;
    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
            final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);
            final String sessionId = accessor.getSessionId();
            try {
                if (accessor.getFirstNativeHeader(REGISTER_HEADER).equals("true")) {
                    StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                    headerAccessor.setMessage(registerService.registration(username, password));
                    headerAccessor.setSessionId(sessionId);
                    clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
                    return null;
                }
            } catch (NullPointerException e) {

            }

            try {
                final UsernamePasswordAuthenticationToken user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, password);
                accessor.setUser(user);
            } catch (AuthenticationException error) {
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
                headerAccessor.setMessage(error.getMessage());
                headerAccessor.setSessionId(sessionId);
                clientOutboundChannel.send(MessageBuilder.createMessage(new byte[0], headerAccessor.getMessageHeaders()));
                return null;
            }

        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel messageChannel, boolean b) {

    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel messageChannel, boolean b, Exception e) {

    }

    @Override
    public boolean preReceive(MessageChannel messageChannel) {
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {
        return null;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel messageChannel, Exception e) {

    }
}