package com.baeldung.jcache;

import static org.junit.Assert.assertEquals;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import org.junit.Before;
import org.junit.Test;

public class EventListenerTest {

    private Cache<String, String> cache;
    private SimpleCacheEntryListener listener;
    MutableCacheEntryListenerConfiguration<String, String> listenerConfiguration;

    @Before
    public void setup() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();
        MutableConfiguration<String, String> config = new MutableConfiguration<String, String>();
        this.cache = cacheManager.createCache("MyCache", config);
        this.listener = new SimpleCacheEntryListener();
    }

    @Test
    public void whenRunEvent_thenCorrect() throws InterruptedException {
        this.listenerConfiguration = new MutableCacheEntryListenerConfiguration<String, String>(FactoryBuilder.factoryOf(this.listener), null, false, true);
        this.cache.registerCacheEntryListener(this.listenerConfiguration);

        assertEquals(false, this.listener.getCreated());

        this.cache.put("key", "value");
        assertEquals(true, this.listener.getCreated());
        assertEquals(false, this.listener.getUpdated());

        this.cache.put("key", "newValue");
        assertEquals(true, this.listener.getUpdated());
    }

}
