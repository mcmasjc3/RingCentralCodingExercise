package mcmaster.ringcentral.queuing;

import java.util.concurrent.ExecutorService;

public class QueueSystem {
  private final ExecutorService executorService;
  private final MessageConsumer messageConsumer;

  @AutoWired
  public QueueSystem(ExecutorService executorService, MessageConsumer messageConsumer) {
    this.executorService = executorService;
    this.messageConsumer = messageConsumer;
    executorService.submit(messageConsumer);
  }
}
