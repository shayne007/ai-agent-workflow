package aiagent.domain.voice.valueobject;

/**
 * Enum representing different types of responses that can be sent to the customer.
 */
public enum ResponseType {
    TEXT,           // Plain text response
    AUDIO,          // Audio response
    IMAGE,          // Image response
    RICH_TEXT,      // Rich text with formatting
    CARD,           // Card-based UI element
    QUICK_REPLY     // Quick reply buttons
}