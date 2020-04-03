# RingCentralCodingExercise

## Part 1 - Refactoring

The refactored code is in the `refactoring` package.

I pulled the Service and RepoClass classes into separate Java files, and wrote a User class with 
a Builder.  Not having the User class available made IntelliJ really unhappy, and I wanted the 
refactoring tools.  I ordinarily would consider renaming `first` and `last` to `firstName` and 
`lastName` for clarity, but since other code presumably is using User, I decided not to do so.

Since both existing classes had public constructors, I didn't see the need for any methods to 
be static.  Those make unit testing harder, so I try not to make methods static unless absolutely 
necessary.

The RepoClass had a reference to a Service instance, but only so it could call `canAccessUsers()`,
which immediately called back to `RepoClass.canAccessUsers()`.  I removed the reference and made 
RepoClass call its own version directly, which means `Service,canAccessUsers()` could go away.

We were checking the access permissions in both `Service.saveUsers()` and `RepoClass.saveUsers()`.
This was unnecessary, so I removed it from Service.

We also were creating a new Session in each RepoClass method.  These are reusable, so I injected a 
Session instance in the constructor and stored it in a field at RepoClass instantiation.  I don't 
like field injection (via @AutoWired).  It means fields cannot be final and must be visible for 
testing.  I changed to constructor injection instead.

I also like lambdas better than for loops, so I changed those as well.

## Part 2 - In-memory Queue

The code for this part is in the `queuing` package

We implemented something very similar at Avero using Kafka, but that is not in-memory.

I will implement this system with a main `QueueSystem` class, which sets up an `ExecutorService`
and starts a `MessageConsumer` which pulls messages from a `java.util.ConcurrentLinkedDeque<Message>`.
When the MessageConsumer gets a message, it decides which `MessageProcessor` to instantiate.  
MessageProcessor is an interface which extends Runnable and defines a Factory interface for 
creating instances of implementing classes.  The MessageProcessor instance is added to 
the ExecutorService for asynchronous processing.

A Message contains a `topic` field that tells the MessageConsumer which MessageProcessor should 
handle the message.  The message also has a `payload` field that holds message content.  For purposes 
of this exercise, the payload will just be a String for simplicity. In a real system I would make 
Message generic, and the payload would be an Object that could hold structured content, like a 
Proto Buffer.  Even a String might work if it contained something like JSON which could be parsed. 

If I were writing this for real, I would probably add code to notify the MessageConsumer when a
Message is added. For this exercise I will just poll in a loop.

Clients access the system by defining a message topic and implementing a MessageProcessor with a 
`run()` method to handle the payload.  The processor is added to the Map in the MessageConsumer.
The client gets a MessageProducer instance and calls `addMessage()` with the topic and payload.

There obviously is a lot more to add, but I think this is sufficient for this exercise.

## Part 3 - JWT parsing

For this exercise, I wrote a `JwtParser` class to do the main work.  It parses an encoded JWT and
returns a `Map<String, String>` containing the keys and values from the payload.

It handles multiple authentication schemes by having a `Map<String, JwtAuthenticator>`.  The key
is the algorithm specified in the header.  `JwtAuthenticator` is an interface which can be
implemented with any desired algorithm.  For simplicity, I just hard-coded a static map in the
class.  In real-life I would inject the Map in the constructor.

I created a `JwtHeader` class, since I know the values.  For the payload, I don't know which
properties to expect, so I return a simple Map.  If the properties were well-defined, it would
be safer to create a `JwtPayload` class.

I wrote a unit test to exercise the code.  This is how I normally test anything I write.  I didn't
write tests for the refactoring piece because it doesn't compile and I don't know which
dependencies I need.  I didn'd for the queueing piece for the same reason.

