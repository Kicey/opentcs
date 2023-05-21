package site.kicey.strategies.rpc.scheduling;

import com.google.inject.Inject;
import org.opentcs.components.kernel.ResourceAllocationException;
import org.opentcs.components.kernel.Scheduler;
import org.opentcs.data.model.TCSResource;
import site.kicey.opentcs.strategies.rpc.api.RpcScheduler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RpcSchedulerProviderProxy implements RpcScheduler {

  private final Scheduler scheduler;

  @Inject
  public RpcSchedulerProviderProxy(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  @Override
  public void initialize() {
    scheduler.initialize();
  }

  @Override
  public boolean isInitialized() {
    return scheduler.isInitialized();
  }

  @Override
  public void terminate() {
    scheduler.terminate();
  }

  @Override
  public void claim(@Nonnull Client client, @Nonnull List<Set<TCSResource<?>>> resourceSequence) {
    scheduler.claim(client, resourceSequence);
  }

  @Override
  public void updateProgressIndex(@Nonnull Client client, int index) throws IllegalArgumentException {
    scheduler.updateProgressIndex(client, index);
  }

  @Override
  public void unclaim(@Nonnull Client client) throws IllegalArgumentException {
    scheduler.unclaim(client);
  }

  @Override
  public void allocate(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) throws IllegalArgumentException {
    scheduler.allocate(client, resources);
  }

  @Override
  public boolean mayAllocateNow(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) {
    return scheduler.mayAllocateNow(client, resources);
  }

  @Override
  public void allocateNow(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) throws ResourceAllocationException {
    scheduler.allocateNow(client, resources);
  }

  @Override
  public void free(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) {
    scheduler.free(client, resources);
  }

  @Override
  public void freeAll(@Nonnull Client client) {
    scheduler.freeAll(client);
  }

  @Override
  public void clearPendingAllocations(@Nonnull Client client) {
    scheduler.clearPendingAllocations(client);
  }

  @Override
  public void reschedule() {
    scheduler.reschedule();
  }

  @Nonnull
  @Override
  public Map<String, Set<TCSResource<?>>> getAllocations() {
    return scheduler.getAllocations();
  }

  @Override
  public void preparationSuccessful(@Nonnull Module module, @Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) {
    scheduler.preparationSuccessful(module, client, resources);
  }
}
