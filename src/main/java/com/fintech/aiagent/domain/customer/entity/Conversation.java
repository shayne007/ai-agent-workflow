package com.fintech.aiagent.domain.customer.entity;

import com.fintech.aiagent.domain.customer.valueobject.ConversationId;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a conversation between a customer and the AI agent.
 */
@Entity
@Table(name = "conversations")
public class Conversation {
    
    @Id
    @Column(name = "conversation_id")
    private String id;
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "start_time")
    private Instant startTime;
    
    @Column(name = "end_time")
    private Instant endTime;
    
    // Add the missing lastActivityTime field
    @Column(name = "last_activity_time")
    private Instant lastActivityTime;
    
    @ElementCollection
    @CollectionTable(name = "conversation_messages", joinColumns = @JoinColumn(name = "conversation_id"))
    @Column(name = "message")
    private List<String> messages;
    
    @Column(name = "current_intent")
    private String currentIntent;
    
    @Column(name = "active")
    private boolean active;
    
    @Column(name = "turn_count")
    private int turnCount;

    // Default constructor required by JPA
    public Conversation() {
        this.startTime = Instant.now();
        this.lastActivityTime = Instant.now(); // Initialize lastActivityTime
        this.messages = new ArrayList<>();
        this.active = true;
        this.turnCount = 0;
    }
    
    // Constructor that takes ConversationId, CustomerId, and LocalDateTime
    public Conversation(ConversationId conversationId, CustomerId customerId, LocalDateTime startTime) {
        this.id = conversationId != null ? conversationId.getId() : null;
        this.customerId = customerId != null ? customerId.getId() : null;
        this.startTime = startTime != null ? startTime.toInstant(java.time.ZoneOffset.UTC) : Instant.now();
        this.lastActivityTime = this.startTime; // Initialize lastActivityTime
        this.messages = new ArrayList<>();
        this.active = true;
        this.turnCount = 0;
    }

    // Getter and setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and setter for ConversationId (value object)
    public ConversationId getConversationId() {
        return new ConversationId(id);
    }

    public void setConversationId(ConversationId conversationId) {
        this.id = conversationId != null ? conversationId.getId() : null;
    }

    // Getter and setter for CustomerId (value object)
    public CustomerId getCustomerId() {
        return new CustomerId(customerId);
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId != null ? customerId.getId() : null;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getCurrentIntent() {
        return currentIntent;
    }

    public void setCurrentIntent(String currentIntent) {
        this.currentIntent = currentIntent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    // Add getter and setter for lastActivityTime
    public Instant getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(Instant lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
    }
    
    // Update addMessage to also update lastActivityTime
    public void addMessage(String message) {
        this.messages.add(message);
        this.lastActivityTime = Instant.now(); // Update lastActivityTime when a message is added
    }
    
    // Update incrementTurnCount to also update lastActivityTime
    public void incrementTurnCount() {
        this.turnCount++;
        this.lastActivityTime = Instant.now(); // Update lastActivityTime when turn count is incremented
    }

    public void endConversation() {
        this.active = false;
        this.endTime = Instant.now();
    }
}