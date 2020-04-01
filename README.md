# RingCentralCodingExercise

## Part 1 - Refactoring

The refactored code is in the refactoring package.

I pulled the two classes into separate Java files, and wrote a User class with a Builder.
Not having the User class available made IntelliJ really unhappy.

Since both existing classes had public constructors, I didn't see the need for any
methods to be static.  Those make unit testing harder, so I try not to make
methods static unless absolutely necessary.

The RepoClass had a reference to a Service instance, but only so it could call `canAccessUsers()`,
which immediately called back to `RepoClass.canAccessUsers()`.  I removed the reference and made RepoClass call
its own version directly, which means `Service,canAccessUsers()` could go away.

We were checking the access permissions in both `Service.saveUsers()` and `RepoClass.saveUsers()`.
This was unnecessary, so I removed it from Service.

We also were creating a new Session in each RepoClass method.  These are reusable,
so I created a single Session instance and stored it in a field at RepoClass instantiation.

I also like lambdas better than for loops, so I changed those as well.




