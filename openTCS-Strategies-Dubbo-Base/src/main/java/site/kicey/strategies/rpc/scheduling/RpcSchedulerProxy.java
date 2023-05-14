package site.kicey.strategies.rpc.scheduling;

import com.google.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.opentcs.components.kernel.ResourceAllocationException;
import org.opentcs.components.kernel.Scheduler;
import org.opentcs.data.model.TCSResource;
import site.kicey.opentcs.strategies.rpc.api.RpcScheduler;

/**
 * Rpc scheduler based on Dubbo.
 */
public class RpcSchedulerProxy implements Scheduler {

  private final RpcScheduler rpcScheduler;

  @Inject
  public RpcSchedulerProxy(RpcScheduler rpcScheduler){
    this.rpcScheduler = rpcScheduler;
  }

  /**
   * (Re-)Initializes this component before it is being used.
   */
  @Override
  public void initialize() {
    rpcScheduler.initialize();
  }

  /**
   * Checks whether this component is initialized.
   *
   * @return <code>true</code> if, and only if, this component is initialized.
   */
  @Override
  public boolean isInitialized() {
    return rpcScheduler.isInitialized();
  }

  /**
   * Terminates the instance and frees resources.
   */
  @Override
  public void terminate() {
    rpcScheduler.terminate();
  }

  /**
   * Sets/Updates the resource claim for a vehicle.
   * <p>
   * <em>Claimed</em> resources are resources that a vehicle will eventually require for executing
   * its movements in the future, but for which it does not request allocation, yet. Claiming
   * resources provides information to the scheduler about future allocations <em>and their intended
   * order</em>, allowing the scheduler to consider these information for its resource planning.
   * </p>
   * <p>
   * Resources can be claimed by multiple vehicles at the same time. This is different from
   * allocations: Only a single vehicle can allocate a resource at the same time.
   * </p>
   *
   * @param client           The client claiming the resources.
   * @param resourceSequence The sequence of resources claimed. May be empty to clear the client's
   *                         claim.
   */
  @Override
  public void claim(@Nonnull Client client, @Nonnull List<Set<TCSResource<?>>> resourceSequence) {
    rpcScheduler.claim(client, resourceSequence);
  }

  /**
   * Notifies the scheduler that the given client has now reached the given index in its claimed
   * resource sequence, and that the client does not need the resources preceding the index in the
   * sequence, any more.
   *
   * @param client The client.
   * @param index  The new index in the client's claimed resource sequence.
   * @throws IllegalArgumentException If the client does not hold a claim, or if the new index is
   *                                  larger than a valid index in its claim's resource sequence, or
   *                                  if the new index is not larger than the current index.
   * @deprecated Stick to {@link #claim(Client, List) claim()} and
   * {@link #allocate(Client, Set) allocate()}. They implicitly update both the set of claimed and
   * the set of allocated resources.
   */
  @Override
  public void updateProgressIndex(@Nonnull Client client, int index)
      throws IllegalArgumentException {
    rpcScheduler.updateProgressIndex(client, index);
  }

  /**
   * Unclaims a set of resources claimed by a vehicle.
   *
   * @param client The client unclaiming the resources.
   * @throws IllegalArgumentException If the given client does not hold a claim.
   * @deprecated Use {@link #claim(Client, List)} with an empty list, instead.
   */
  @Override
  public void unclaim(@Nonnull Client client) throws IllegalArgumentException {
    rpcScheduler.unclaim(client);
  }

  /**
   * Requests allocation of the given resources. The client will be informed via a callback to
   * {@link Client#allocationSuccessful(Set)} or {@link Client#allocationFailed(Set)} whether the
   * allocation was successful or not.
   * <ul>
   * <li>
   * Clients may only allocate resources in the order they have previously
   * {@link #claim(Client, List) claim()}ed them.
   * </li>
   * <li>
   * Upon allocation, the scheduler will implicitly remove the set of allocated resources from (the
   * head of) the client's claim sequence.
   * </li>
   * <li>
   * As a result, a client may only allocate the set of resources at the head of its claim sequence.
   * </li>
   * </ul>
   *
   * @param client    The client requesting the resources.
   * @param resources The resources to be allocated.
   * @throws IllegalArgumentException If the set of resources to be allocated is not equal to the
   *                                  <em>next</em> set in the sequence of currently claimed
   *                                  resources, or if the client has already requested resources
   *                                  that have not yet been granted.
   * @see #claim(Client, List)
   */
  @Override
  public void allocate(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources)
      throws IllegalArgumentException {
    rpcScheduler.allocate(client, resources);
  }

  /**
   * Checks if the resulting system state is safe if the given set of resources would be allocated
   * by the given client <em>immediately</em>.
   *
   * @param client    The client requesting the resources.
   * @param resources The requested resources.
   * @return {@code true} if the given resources are safe to be allocated by the given client,
   * otherwise {@code false}.
   */
  @Override
  public boolean mayAllocateNow(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) {
    return rpcScheduler.mayAllocateNow(client, resources);
  }

  /**
   * Informs the scheduler that a set of resources are to be allocated for the given client
   * <em>immediately</em>.
   * <p>
   * Note the following:
   * </p>
   * <ul>
   * <li>
   * This method should only be called in urgent/emergency cases, for instance if a vehicle has been
   * moved to a different point manually, which has to be reflected by resource allocation in the
   * scheduler.
   * </li>
   * <li>
   * Unlike
   * {@link #allocate(Client, Set) allocate()},
   * this method does not block, i.e. the operation happens synchronously.
   * </li>
   * <li>
   * This method does <em>not</em> implicitly deallocate or unclaim any other resources for the
   * client.
   * </li>
   * </ul>
   *
   * @param client    The client requesting the resources.
   * @param resources The resources requested.
   * @throws ResourceAllocationException If it's impossible to allocate the given set of resources
   *                                     for the given client.
   */
  @Override
  public void allocateNow(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources)
      throws ResourceAllocationException {
    rpcScheduler.allocateNow(client, resources);
  }

  /**
   * Releases a set of resources allocated by a client.
   *
   * @param client    The client releasing the resources.
   * @param resources The resources released. Any resources in the given set not allocated by the
   *                  given client are ignored.
   */
  @Override
  public void free(@Nonnull Client client, @Nonnull Set<TCSResource<?>> resources) {
    rpcScheduler.free(client, resources);
  }

  /**
   * Releases all resources allocated by the given client.
   *
   * @param client The client.
   */
  @Override
  public void freeAll(@Nonnull Client client) {
    rpcScheduler.freeAll(client);
  }

  /**
   * Releases all pending resource allocations for the given client.
   *
   * @param client The client.
   */
  @Override
  public void clearPendingAllocations(@Nonnull Client client) {
    rpcScheduler.clearPendingAllocations(client);
  }

  /**
   * Explicitly triggers a rescheduling run during which the scheduler tries to allocate resources
   * for all waiting clients.
   */
  @Override
  public void reschedule() {
    rpcScheduler.reschedule();
  }

  /**
   * Returns all resource allocations as a map of client IDs to resources.
   *
   * @return All resource allocations as a map of client IDs to resources.
   */
  @Nonnull
  @Override
  public Map<String, Set<TCSResource<?>>> getAllocations() {
    return rpcScheduler.getAllocations();
  }

  /**
   * Informs the scheduler that a set of resources was successfully prepared in order of allocating
   * them to a client.
   *
   * @param module    The module a preparation was necessary for.
   * @param client    The client that requested the preparation/allocation.
   * @param resources The resources that are now prepared for the client.
   */
  @Override
  public void preparationSuccessful(@Nonnull Module module, @Nonnull Client client,
      @Nonnull Set<TCSResource<?>> resources) {
    rpcScheduler.preparationSuccessful(module, client, resources);
  }
}
