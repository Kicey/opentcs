package site.kicey.opentcs.strategies.rpc.proxy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.opentcs.components.kernel.ResourceAllocationException;
import org.opentcs.components.kernel.Scheduler;
import org.opentcs.data.model.TCSResource;

public class SchedulerRpcProxy implements Scheduler {

  @Override
  public void initialize() {

  }

  @Override
  public boolean isInitialized() {
    return false;
  }

  @Override
  public void terminate() {

  }

  @Override
  public void claim(@Nonnull Client client, @Nonnull List<Set<TCSResource<?>>> resourceSequence) {

  }

  @Override
  public void updateProgressIndex(@Nonnull Client client, int index)
      throws IllegalArgumentException {
    Scheduler.super.updateProgressIndex(client, index);
  }

  @Override
  public void unclaim(@Nonnull Client client) throws IllegalArgumentException {
    Scheduler.super.unclaim(client);
  }

  @Override
  public void allocate(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources)
      throws IllegalArgumentException {

  }

  @Override
  public boolean mayAllocateNow(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) {
    return Scheduler.super.mayAllocateNow(client, resources);
  }

  @Override
  public void allocateNow(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources)
      throws ResourceAllocationException {

  }

  @Override
  public void free(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) {

  }

  @Override
  public void freeAll(@Nonnull Client client) {

  }

  @Override
  public void clearPendingAllocations(@Nonnull Client client) {
    Scheduler.super.clearPendingAllocations(client);
  }

  @Override
  public void reschedule() {
    Scheduler.super.reschedule();
  }

  @Nonnull
  @Override
  public Map<String, Set<TCSResource<?>>> getAllocations() {
    return null;
  }

  @Override
  public void preparationSuccessful(@Nonnull Module module, @Nonnull Client client,
      @Nonnull Set<TCSResource<?>> resources) {

  }
}
