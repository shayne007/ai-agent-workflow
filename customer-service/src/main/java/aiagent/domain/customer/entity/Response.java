package aiagent.domain.customer.entity;

import aiagent.domain.customer.valueobject.ResponseType;
import jakarta.persistence.*;

/**
 * Entity representing a response in a conversation.
 */
@Entity
@Table(name = "responses")
public class Response {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content", length = 4000)
    private String content;
    
    @ElementCollection
    @CollectionTable(name = "response_suggested_actions", joinColumns = @JoinColumn(name = "response_id"))
    @Column(name = "action")
    private String[] suggestedActions;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ResponseType type;
    
    @Column(name = "context")
    private String context;

    public Response() {
        this.type = ResponseType.TEXT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getSuggestedActions() {
        return suggestedActions;
    }

    public void setSuggestedActions(String[] suggestedActions) {
        this.suggestedActions = suggestedActions;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}