package com.tanji.test.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author shenjh
 * @version 1.0
 * @since 2024-10-11 13:29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class EsConfig extends AbstractElasticsearchConfiguration {

    private String host;
    private Integer port;

    /** 
     * 新版的 spring-data-elasticsearch 中，ElasticsearchRestTemplate 代替了原来的 ElasticsearchTemplate，
     * 原因是 ElasticsearchTemplate 基于 TransportClient，TransportClient 即将在 8.x 以后的版本中移除。所
     * 以，我们推荐使用 ElasticsearchRestTemplate。<br/>
     *
     * ElasticsearchRestTemplate 基于 RestHighLevelClient 客户端的。需要自定义配置类，继承
     * AbstractElasticsearchConfiguration，并实现 elasticsearchClient()抽象方法，创建 RestHighLevelClient 对 象。
     * @return org.elasticsearch.client.RestHighLevelClient
     * @author shenjh
     * @since 2024/10/11 13:55
     */ 
    @Override
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port));
        return new RestHighLevelClient(builder);
    }
}
