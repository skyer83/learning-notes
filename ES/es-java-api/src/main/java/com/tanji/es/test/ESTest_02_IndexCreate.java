package com.tanji.es.test;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 索引 - 创建
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_02_IndexCreate {

    // 直接使用 log4j
    private static final Logger logger = LogManager.getLogger(ESTest_02_IndexCreate.class);
    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_02_IndexCreate.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 创建索引
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("user");
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            // 响应信息
            boolean acknowledged = createIndexResponse.isAcknowledged();
            logger.info("log4j:::" + acknowledged);
            log.debug("slf4j:::" + acknowledged);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
