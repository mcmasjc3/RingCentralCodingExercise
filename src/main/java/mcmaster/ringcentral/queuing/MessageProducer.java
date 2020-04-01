package mcmaster.ringcentral.queuing;

import java.util.concurrent.ConcurrentLinkedDeque;

public class MessageProducer {
  private final ConcurrentLinkedDeque<Message> messageQueue;

  @AutoWired
  public MessageProducer(ConcurrentLinkedDeque<Message> messageQueue) {
    this.messageQueue = messageQueue;
  }

  public boolean addMessage(String topic, String payload) {
    return messageQueue.add(new Message(topic, payload));
  }
}
