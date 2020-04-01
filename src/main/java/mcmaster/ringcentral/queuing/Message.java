package mcmaster.ringcentral.queuing;

public class Message {
  private final String topic;
  private final String payload;

  public Message(String topic, String payload) {
    this.topic = topic;
    this.payload = payload;
  }

  public String getTopic() {
    return topic;
  }

  public String getPayload() {
    return payload;
  }
}
