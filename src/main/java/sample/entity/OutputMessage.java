package sample.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_message")
public class OutputMessage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String sender;
    private String message;
    private String topic;
    private Date time = new Date();

    public OutputMessage() {}

    public OutputMessage(String from,String message)
    {
	this.sender = from;
	this.message = message;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String from)
    {
        this.sender = from;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public Date getTime()
    {
        return time;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
