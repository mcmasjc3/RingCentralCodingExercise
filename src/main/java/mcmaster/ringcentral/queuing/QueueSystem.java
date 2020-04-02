package mcmaster.ringcentral.queuing;

import java.util.concurrent.ExecutorService;

public class QueueSystem {
  @AutoWired
  public QueueSystem(ExecutorService executorService, MessageConsumer messageConsumer) {
    executorService.submit(messageConsumer);
  }
}
