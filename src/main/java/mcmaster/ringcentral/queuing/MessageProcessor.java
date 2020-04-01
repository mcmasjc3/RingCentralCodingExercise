package mcmaster.ringcentral.queuing;

public interface MessageProcessor extends Runnable {
  interface Factory {
    MessageProcessor create(Message message);
  }
}
