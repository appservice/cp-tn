package eu.canpack.fip.config;

import eu.canpack.fip.bo.attachment.Attachment;
import eu.canpack.fip.bo.client.Client;
import eu.canpack.fip.bo.commercialPart.CommercialPart;
import eu.canpack.fip.bo.drawing.Drawing;
import eu.canpack.fip.bo.estimation.Estimation;
import eu.canpack.fip.bo.machine.Machine;
import eu.canpack.fip.bo.operation.Operation;
import eu.canpack.fip.bo.order.Order;
import eu.canpack.fip.bo.technologyCard.TechnologyCard;
import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache("users", jcacheConfiguration);
            cm.createCache(eu.canpack.fip.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(eu.canpack.fip.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(eu.canpack.fip.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(Order.class.getName(), jcacheConfiguration);
            cm.createCache(Order.class.getName() + ".estimations", jcacheConfiguration);
            cm.createCache(Estimation.class.getName(), jcacheConfiguration);
            cm.createCache(Estimation.class.getName() + ".drawings", jcacheConfiguration);
            cm.createCache(Estimation.class.getName() + ".operations", jcacheConfiguration);
            cm.createCache(Estimation.class.getName() + ".commercialParts", jcacheConfiguration);
            cm.createCache(Operation.class.getName(), jcacheConfiguration);

//            cm.createCache(Operation.class.getName() + ".machines", jcacheConfiguration);
            cm.createCache(Machine.class.getName(), jcacheConfiguration);
            cm.createCache(Machine.class.getName() + ".operations", jcacheConfiguration);
            cm.createCache(Drawing.class.getName(), jcacheConfiguration);
            cm.createCache(Drawing.class.getName() + ".attachments", jcacheConfiguration);
            cm.createCache(Client.class.getName(), jcacheConfiguration);
            cm.createCache(Client.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(Client.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(Attachment.class.getName(), jcacheConfiguration);
            cm.createCache(eu.canpack.fip.domain.Unit.class.getName(), jcacheConfiguration);
            cm.createCache(CommercialPart.class.getName(), jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Order.class.getName(), jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Order.class.getName() + ".estimations", jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Estimation.class.getName(), jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Estimation.class.getName() + ".drawings", jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Estimation.class.getName() + ".operations", jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Operation.class.getName(), jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Operation.class.getName() + ".machines", jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Machine.class.getName(), jcacheConfiguration);
//            cm.createCache(eu.canpack.fip.domain.Drawing.class.getName(), jcacheConfiguration);
            cm.createCache(TechnologyCard.class.getName(), jcacheConfiguration);
            cm.createCache(TechnologyCard.class.getName() + ".operations", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
