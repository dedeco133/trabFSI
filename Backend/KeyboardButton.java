import java.util.Objects;

import org.telegram.telegrambots.api.interfaces.InputBotApiObject;
import org.telegram.telegrambots.api.interfaces.Validable;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyboardButton implements InputBotApiObject, Validable{
	  private static final String TEXT_FIELD = "text";
	    private static final String REQUEST_CONTACT_FIELD = "request_contact";
	    private static final String REQUEST_LOCATION_FIELD = "request_location";
	    /**
	     * Text of the button.
	     * If none of the optional fields are used, it will be sent to the bot as a message when the button is pressed
	     */
	    @JsonProperty(TEXT_FIELD)
	    private String text;
	    /**
	     * Optional.
	     * If True, the user's phone number will be sent as a contact when the button is pressed.
	     * Available in private chats only
	     */
	    @JsonProperty(REQUEST_CONTACT_FIELD)
	    private Boolean requestContact;
	    /**
	     * Optional.
	     * If True, the user's current location will be sent when the button is pressed.
	     * Available in private chats only
	     */
	    @JsonProperty(REQUEST_LOCATION_FIELD)
	    private Boolean requestLocation;

	    public KeyboardButton() {
	        super();
	    }

	    public KeyboardButton(String text) {
	        super();
	        this.text = text;
	    }

	    public String getText() {
	        return text;
	    }

	    public KeyboardButton setText(String text) {
	        this.text = text;
	        return this;
	    }

	    public Boolean getRequestContact() {
	        return requestContact;
	    }

	    public KeyboardButton setRequestContact(Boolean requestContact) {
	        this.requestContact = requestContact;
	        return this;
	    }

	    public Boolean getRequestLocation() {
	        return requestLocation;
	    }

	    public KeyboardButton setRequestLocation(Boolean requestLocation) {
	        this.requestLocation = requestLocation;
	        return this;
	    }

	    @Override
	    public void validate() throws TelegramApiValidationException {
	        if (text == null || text.isEmpty()) {
	            throw new TelegramApiValidationException("Text parameter can't be empty", this);
	        }
	        if (requestContact != null && requestLocation != null && requestContact && requestLocation) {
	            throw new TelegramApiValidationException("Cant request contact and location at the same time", this);
	        }
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (o == this) return true;
	        if (!(o instanceof KeyboardButton)) {
	            return false;
	        }
	        KeyboardButton keyboardButton = (KeyboardButton) o;
	        return Objects.equals(requestContact, keyboardButton.requestContact)
	                && Objects.equals(requestLocation, keyboardButton.requestLocation)
	                && Objects.equals(text, keyboardButton.text)
	                ;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(
	                requestContact,
	                requestLocation,
	                text);
	    }

	    @Override
	    public String toString() {
	        return "KeyboardButton{" +
	                "text=" + text +
	                ", requestContact=" + requestContact +
	                ", requestLocation=" + requestLocation +
	                '}';
	    }
	}

