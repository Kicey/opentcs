package site.kicey.strategies.provider.router;

import javax.inject.Singleton;
import org.opentcs.components.kernel.routing.GroupMapper;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.opentcs.strategies.rpc.proxy.DispatcherRpcProxy;
import site.kicey.opentcs.strategies.rpc.proxy.PeripheralJobDispatcherRpcProxy;
import site.kicey.opentcs.strategies.rpc.proxy.SchedulerRpcProxy;
import site.kicey.strategies.provider.router.routing.DefaultRouter;
import site.kicey.strategies.provider.router.routing.DefaultRouterConfiguration;
import site.kicey.strategies.provider.router.routing.DefaultRoutingGroupMapper;
import site.kicey.strategies.provider.router.routing.PointRouterFactory;
import site.kicey.strategies.provider.router.routing.edgeevaluator.EdgeEvaluatorComposite;
import site.kicey.strategies.provider.router.routing.edgeevaluator.EdgeEvaluatorDistance;
import site.kicey.strategies.provider.router.routing.edgeevaluator.EdgeEvaluatorExplicitProperties;
import site.kicey.strategies.provider.router.routing.edgeevaluator.EdgeEvaluatorHops;
import site.kicey.strategies.provider.router.routing.edgeevaluator.EdgeEvaluatorTravelTime;
import site.kicey.strategies.provider.router.routing.edgeevaluator.ExplicitPropertiesConfiguration;
import site.kicey.strategies.provider.router.routing.jgrapht.BellmanFordPointRouterFactory;
import site.kicey.strategies.provider.router.routing.jgrapht.DefaultModelGraphMapper;
import site.kicey.strategies.provider.router.routing.jgrapht.DijkstraPointRouterFactory;
import site.kicey.strategies.provider.router.routing.jgrapht.FloydWarshallPointRouterFactory;
import site.kicey.strategies.provider.router.routing.jgrapht.ModelGraphMapper;
import site.kicey.strategies.provider.router.routing.jgrapht.ShortestPathConfiguration;

public class RouterProviderModule extends KernelInjectionModule {
  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(RouterProviderModule.class);

  /**
   * Creates a new instance.
   */
  public RouterProviderModule() {
  }

  @Override
  protected void configure() {
    configureRouterDependencies();
    bindRouter(DefaultRouter.class);
    bindDispatcher(DispatcherRpcProxy.class);
    bindPeripheralJobDispatcher(PeripheralJobDispatcherRpcProxy.class);
    bindScheduler(SchedulerRpcProxy.class);
  }

  private void configureRouterDependencies() {
    bind(DefaultRouterConfiguration.class)
        .toInstance(getConfigBindingProvider().get(DefaultRouterConfiguration.PREFIX,
            DefaultRouterConfiguration.class));

    ShortestPathConfiguration spConfiguration
        = getConfigBindingProvider().get(ShortestPathConfiguration.PREFIX,
        ShortestPathConfiguration.class);
    bind(ShortestPathConfiguration.class)
        .toInstance(spConfiguration);

    bind(ModelGraphMapper.class)
        .to(DefaultModelGraphMapper.class);

    switch (spConfiguration.algorithm()) {
      case DIJKSTRA:
        bind(PointRouterFactory.class)
            .to(DijkstraPointRouterFactory.class);
        break;
      case BELLMAN_FORD:
        bind(PointRouterFactory.class)
            .to(BellmanFordPointRouterFactory.class);
        break;
      case FLOYD_WARSHALL:
        bind(PointRouterFactory.class)
            .to(FloydWarshallPointRouterFactory.class);
        break;
      default:
        LOG.warn("Unhandled algorithm selected ({}), falling back to Dijkstra's algorithm.",
            spConfiguration.algorithm());
        bind(PointRouterFactory.class)
            .to(DijkstraPointRouterFactory.class);
    }

    edgeEvaluatorBinder()
        .addBinding(EdgeEvaluatorDistance.CONFIGURATION_KEY)
        .to(EdgeEvaluatorDistance.class);
    edgeEvaluatorBinder()
        .addBinding(EdgeEvaluatorExplicitProperties.CONFIGURATION_KEY)
        .to(EdgeEvaluatorExplicitProperties.class);
    edgeEvaluatorBinder()
        .addBinding(EdgeEvaluatorHops.CONFIGURATION_KEY)
        .to(EdgeEvaluatorHops.class);
    edgeEvaluatorBinder()
        .addBinding(EdgeEvaluatorTravelTime.CONFIGURATION_KEY)
        .to(EdgeEvaluatorTravelTime.class);

    bind(EdgeEvaluatorComposite.class)
        .in(Singleton.class);

    bind(ExplicitPropertiesConfiguration.class)
        .toInstance(getConfigBindingProvider().get(ExplicitPropertiesConfiguration.PREFIX,
            ExplicitPropertiesConfiguration.class));

    bind(DefaultRoutingGroupMapper.class)
        .in(Singleton.class);
    bind(GroupMapper.class)
        .to(DefaultRoutingGroupMapper.class);
  }
}
