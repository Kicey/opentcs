/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import org.opentcs.components.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.phase.AssignFreePeripheralsPhase;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.phase.AssignReservedPeripheralsPhase;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.phase.FinishWithdrawalsPhase;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.phase.ReleasePeripheralsPhase;

/**
 * Performs a full dispatch run.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class FullDispatchTask
    implements Runnable,
               Lifecycle {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(FullDispatchTask.class);

  private final FinishWithdrawalsPhase finishWithdrawalsPhase;
  private final AssignReservedPeripheralsPhase assignReservedPeripheralsPhase;
  private final ReleasePeripheralsPhase releasePeripheralsPhase;
  private final AssignFreePeripheralsPhase assignFreePeripheralsPhase;
  /**
   * Indicates whether this component is enabled.
   */
  private boolean initialized;

  @Inject
  public FullDispatchTask(FinishWithdrawalsPhase finishWithdrawalsPhase,
                          AssignReservedPeripheralsPhase assignReservedPeripheralsPhase,
                          ReleasePeripheralsPhase releasePeripheralsPhase,
                          AssignFreePeripheralsPhase assignFreePeripheralsPhase) {
    this.finishWithdrawalsPhase = requireNonNull(finishWithdrawalsPhase, "finishWithdrawalsPhase");
    this.assignReservedPeripheralsPhase = requireNonNull(assignReservedPeripheralsPhase,
                                                         "assignReservedPeripheralsPhase");
    this.releasePeripheralsPhase = requireNonNull(releasePeripheralsPhase,
                                                  "releasePeripheralsPhase");
    this.assignFreePeripheralsPhase = requireNonNull(assignFreePeripheralsPhase,
                                                     "assignFreePeripheralsPhase");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    finishWithdrawalsPhase.initialize();
    assignReservedPeripheralsPhase.initialize();
    releasePeripheralsPhase.initialize();
    assignFreePeripheralsPhase.initialize();

    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }

    assignFreePeripheralsPhase.terminate();
    releasePeripheralsPhase.terminate();
    assignReservedPeripheralsPhase.terminate();
    finishWithdrawalsPhase.terminate();

    initialized = false;
  }

  @Override
  public void run() {
    LOG.debug("Starting full dispatch run...");

    finishWithdrawalsPhase.run();
    assignReservedPeripheralsPhase.run();
    releasePeripheralsPhase.run();
    assignFreePeripheralsPhase.run();

    LOG.debug("Finished full dispatch run.");
  }
}
