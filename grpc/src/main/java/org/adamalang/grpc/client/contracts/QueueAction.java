package org.adamalang.grpc.client.contracts;

/** an unit of work that sits within a queue for processing */
public abstract class QueueAction<T> {
  private boolean alive;
  private final int errorTimeout;
  private final int errorRejected;

  public QueueAction(int errorTimeout, int errorRejected) {
    this.alive = true;
    this.errorTimeout = errorTimeout;
    this.errorRejected = errorRejected;
  }

  /** is the queue action alive */
  public boolean isAlive() {
    return alive;
  }

  /** execute the item if it is still valid */
  public void execute(T item) {
    if (alive) {
      executeNow(item);
    }
  }

  protected abstract void executeNow(T item);

  /** how the timeout is executed */
  public void killDueToTimeout() {
    alive = false;
    failure(errorTimeout);
  }

  protected abstract void failure(int code);

  public void killDueToReject() {
    alive = false;
    failure(errorRejected);
  }
}
