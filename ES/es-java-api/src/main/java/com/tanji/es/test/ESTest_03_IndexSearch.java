package com.tanji.es.test;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 索引 - 查询
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_03_IndexSearch {

    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_03_IndexSearch.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 获取索引
            GetIndexRequest getIndexRequest = new GetIndexRequest("user");
            GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);

            // 响应信息
            log.info(getIndexResponse.getAliases().toString());
            log.info(getIndexResponse.getMappings().toString());
            log.info(getIndexResponse.getSettings().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
