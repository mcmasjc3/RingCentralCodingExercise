package mcmaster.ringcentral.queuing;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;

/**
 * Implements a consumer for messages added to the queue.
 */
public class MessageConsumer implements Runnable {
  private final ExecutorService executorService;
  private final ConcurrentLinkedDeque<Message> messageQueue;
  private final Map<String, MessageProcessor.Factory> topicProcessorMap;

  /**
   * @param executorService   {@link ExecutorService} for consuming and processing messages
   * @param messageQueue      {@link Deque} of messages added by producers
   * @param topicProcessorMap Map of topics to {@link MessageProcessor.Factory Factories} for creating
   *                          {@link MessageProcessor processors} for messages.F
   */
  @AutoWired
  public MessageConsumer(
      ExecutorService executorService,
      ConcurrentLinkedDeque<Message> messageQueue,
      Map<String, MessageProcessor.Factory> topicProcessorMap) {
    this.executorService = executorService;
    this.messageQueue = messageQueue;
    this.topicProcessorMap = topicProcessorMap;
  }

  /**
   * Poll the Deque for message in an infinite loop. When no more messages are available, sleep.
   */
  @Override
  public void run() {
    while (true) {
      Message message = messageQueue.poll();
      while (message != null) {
        MessageProcessor.Factory factory = topicProcessorMap.get(message.getTopic());
        if (factory != null) {
          MessageProcessor messageProcessor = factory.create(message);
          executorService.submit(messageProcessor);
        }
        message = messageQueue.poll();
      }
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        // Keep going
      }
    }
  }
}
