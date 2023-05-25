/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package site.kicey.strategies.provider.router;

import com.google.common.util.concurrent.Uninterruptibles;
import org.opentcs.access.Kernel;
import org.opentcs.components.kernel.*;
import org.opentcs.customizations.kernel.ActiveInOperatingMode;
import org.opentcs.customizations.kernel.GlobalSyncObject;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.opentcs.kernelbase.KernelApplicationConfiguration;
import org.opentcs.kernelbase.persistence.ModelPersister;
import org.opentcs.kernelbase.workingset.PeripheralJobPoolManager;
import org.opentcs.kernelbase.workingset.PlantModelManager;
import org.opentcs.kernelbase.workingset.TransportOrderPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * This class implements the standard openTCS kernel in normal operation.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class KernelStateOperating
    extends KernelStateOnline {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(KernelStateOperating.class);
  /**
   * The order pool manager.
   */
  private final TransportOrderPoolManager orderPoolManager;
  /**
   * The job pool manager.
   */
  private final PeripheralJobPoolManager jobPoolManager;
  /**
   * This kernel's router.
   */
  private final Router router;
  /**
   * The kernel's executor.
   */
  private final ScheduledExecutorService kernelExecutor;
  /**
   * A task for periodically getting rid of old orders.
   */
  private final OrderCleanerTask orderCleanerTask;
  /**
   * This kernel state's local extensions.
   */
  private final Set<KernelExtension> extensions;
  /**
   * A handle for the cleaner task.
   */
  private ScheduledFuture<?> cleanerTaskFuture;
  /**
   * This instance's <em>initialized</em> flag.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject kernel threads' global synchronization object.
   * @param plantModelManager The plant model manager to be used.
   * @param orderPoolManager The order pool manager to be used.
   * @param jobPoolManager The job pool manager to be used.
   * @param modelPersister The model persister to be used.
   * @param configuration This class's configuration.
   * @param router The router to be used.
   * @param kernelExecutor The kernel executer to be used.
   * @param orderCleanerTask The order cleaner task to be used.
   * @param extensions The kernel extensions to load.
   */
  @Inject
  public KernelStateOperating(@GlobalSyncObject Object globalSyncObject,
                              PlantModelManager plantModelManager,
                              TransportOrderPoolManager orderPoolManager,
                              PeripheralJobPoolManager jobPoolManager,
                              ModelPersister modelPersister,
                              KernelApplicationConfiguration configuration,
                              Router router,
                              @KernelExecutor ScheduledExecutorService kernelExecutor,
                              OrderCleanerTask orderCleanerTask,
                              @ActiveInOperatingMode Set<KernelExtension> extensions) {
    super(globalSyncObject,
          plantModelManager,
          modelPersister,
          configuration.saveModelOnTerminateOperating());
    this.orderPoolManager = requireNonNull(orderPoolManager, "orderPoolManager");
    this.jobPoolManager = requireNonNull(jobPoolManager, "jobPoolManager");
    this.router = requireNonNull(router, "router");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
    this.orderCleanerTask = requireNonNull(orderCleanerTask, "orderCleanerTask");
    this.extensions = requireNonNull(extensions, "extensions");
  }

  // Implementation of interface Kernel starts here.
  @Override
  public void initialize() {
    if (initialized) {
      LOG.debug("Already initialized.");
      return;
    }
    LOG.debug("Initializing operating state...");

    LOG.debug("Initializing router '{}'...", router);
    router.initialize();

    // Start a task for cleaning up old orders periodically.
    cleanerTaskFuture = kernelExecutor.scheduleAtFixedRate(orderCleanerTask,
                                                           orderCleanerTask.getSweepInterval(),
                                                           orderCleanerTask.getSweepInterval(),
                                                           TimeUnit.MILLISECONDS);

    // Start kernel extensions.
    for (KernelExtension extension : extensions) {
      LOG.debug("Initializing kernel extension '{}'...", extension);
      extension.initialize();
    }
    LOG.debug("Finished initializing kernel extensions.");

    initialized = true;

    LOG.debug("Operating state initialized.");
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!initialized) {
      LOG.debug("Not initialized.");
      return;
    }
    LOG.debug("Terminating operating state...");
    super.terminate();

    // Terminate everything that may still use resources.
    for (KernelExtension extension : extensions) {
      LOG.debug("Terminating kernel extension '{}'...", extension);
      extension.terminate();
    }
    LOG.debug("Terminated kernel extensions.");

    // No need to clean up any more - it's all going to be cleaned up very soon.
    cleanerTaskFuture.cancel(false);
    cleanerTaskFuture = null;

    // Terminate strategies.
    LOG.debug("Terminating router '{}'...", router);
    router.terminate();
    // Grant communication adapters etc. some time to settle things.
    Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);

    // Ensure that vehicles do not reference orders any more.

    // Remove all orders and order sequences from the pool.
    orderPoolManager.clear();
    // Remove all peripheral jobs from the pool.
    jobPoolManager.clear();

    initialized = false;

    LOG.debug("Operating state terminated.");
  }

  @Override
  public Kernel.State getState() {
    return Kernel.State.OPERATING;
  }
}
