package ru.filit.mdma.dms.config;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataPageEvictionMode;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Настройка кэширования приложения DMS с помощью Apache Ignite.
 */
@Configuration
public class CacheConfig {

  @Bean
  public String igniteInstanceName() {
    return "igniteInstance";
  }

  @Bean
  public IgniteSpringBean ignite() {
    IgniteSpringBean bean = new IgniteSpringBean();
    bean.setConfiguration(igniteConfiguration());

    return bean;
  }

  private IgniteConfiguration igniteConfiguration() {
    IgniteConfiguration configuration = new IgniteConfiguration();

    configuration.setIgniteInstanceName(igniteInstanceName());
    configuration.setClientMode(false);
    configuration.setGridLogger(new Slf4jLogger());
    configuration.setDataStorageConfiguration(dataStorageConfiguration());
    configuration.setCacheConfiguration(igniteCacheConfiguration());
    configuration.setDiscoverySpi(discoverySpi());

    return configuration;
  }

  private DataStorageConfiguration dataStorageConfiguration() {
    DataStorageConfiguration configuration = new DataStorageConfiguration();
    configuration.setDefaultDataRegionConfiguration(dataRegionConfiguration());

    return configuration;
  }

  private DataRegionConfiguration dataRegionConfiguration() {
    DataRegionConfiguration configuration = new DataRegionConfiguration();
    configuration.setName("Default_Region");
    configuration.setInitialSize(10485760L);       // 10 MB
    configuration.setMaxSize(157286400L);          // 150 MB
    configuration.setPageEvictionMode(DataPageEvictionMode.RANDOM_2_LRU);
    configuration.setMetricsEnabled(true);

    return configuration;
  }

  private CacheConfiguration<String, String> igniteCacheConfiguration() {
    CacheConfiguration<String, String> configuration = new CacheConfiguration<>();
    configuration.setName("tokenCache");
    configuration.setCacheMode(CacheMode.REPLICATED);
    configuration.setBackups(1);
    configuration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
    configuration.setExpiryPolicyFactory(
        AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 60)));
    configuration.setEagerTtl(true);
    configuration.setStatisticsEnabled(true);

    return configuration;
  }

  private TcpDiscoveryIpFinder igniteIpFinder() {
    TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
    ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));

    return ipFinder;
  }

  private DiscoverySpi discoverySpi() {
    TcpDiscoverySpi spi = new TcpDiscoverySpi();
    spi.setIpFinder(igniteIpFinder());

    return spi;
  }
}