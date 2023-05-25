package site.kicey.strategies.provider.router;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;

import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.opentcs.access.Kernel;
import org.opentcs.access.LocalKernel;
import org.opentcs.components.kernel.KernelExtension;
import org.opentcs.components.kernel.services.InternalPlantModelService;
import org.opentcs.customizations.kernel.ActiveInAllModes;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcRouter;
import site.kicey.strategies.rpc.DubboConfiguration;
import site.kicey.strategies.rpc.routing.RpcRouterProviderProxy;

public class ProviderStarter {
  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ProviderStarter.class);
  /**
   * The kernel we're working with.
   */
  private final LocalKernel kernel;
  /**
   * The plant model service.
   */
  private final InternalPlantModelService plantModelService;
  /**
   * The kernel extensions to be registered.
   */
  private final Set<KernelExtension> extensions;
  /**
   * The kernel's executor service.
   */
  private final ScheduledExecutorService kernelExecutor;
  /**
   * The scheduler used as provider.
   */
  private final RpcRouterProviderProxy routerProviderProxy;
  /**
   * Dubbo related configuration.
   */
  private final DubboConfiguration dubboConfiguration;
  /**
   * Creates a new instance.
   *
   * @param kernel The kernel we're working with.
   * @param plantModelService The plant model service.
   * @param extensions The kernel extensions to be registered.
   * @param kernelExecutor The kernel's executor service.
   */
  @Inject
  protected ProviderStarter(LocalKernel kernel,
      InternalPlantModelService plantModelService,
      RpcRouterProviderProxy rpcRouterProviderProxy,
      DubboConfiguration dubboConfiguration,
      @ActiveInAllModes Set<KernelExtension> extensions,
      @KernelExecutor ScheduledExecutorService kernelExecutor) {
    this.kernel = requireNonNull(kernel, "kernel");
    this.plantModelService = requireNonNull(plantModelService, "plantModelService");
    this.routerProviderProxy = requireNonNull(rpcRouterProviderProxy, "rpcRouterProviderProxy");
    this.dubboConfiguration = requireNonNull(dubboConfiguration, "dubboConfiguration");
    this.extensions = requireNonNull(extensions, "extensions");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }

  /**
   * Initializes the system and starts the openTCS kernel including modules.
   *
   * @throws IOException If there was a problem loading model data.
   */
  public void startKernel()
      throws IOException {

    ServiceConfig<RpcRouter> serviceConfig = new ServiceConfig<>();
    serviceConfig.setInterface(RpcRouter.class);
    serviceConfig.setRef(routerProviderProxy);

    DubboBootstrap.getInstance()
        .application(RpcConstant.RPC_ROUTER_PROVIDER_NAME)
        .registry(new RegistryConfig(dubboConfiguration.zookeeperAddress()))
        .protocol(new ProtocolConfig("dubbo", -1))
        .service(serviceConfig)
        .start();

    kernelExecutor.submit(() -> {
      // Register kernel extensions.
      for (KernelExtension extension : extensions) {
        kernel.addKernelExtension(extension);
      }

      // Start local kernel.
      kernel.initialize();
      LOG.debug("Kernel initialized.");

      plantModelService.loadPlantModel();

      kernel.setState(Kernel.State.OPERATING);
    });
  }
}
