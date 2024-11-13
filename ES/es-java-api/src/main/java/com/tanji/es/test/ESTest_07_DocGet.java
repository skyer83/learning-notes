package com.tanji.es.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 文档 - 查询
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_07_DocGet {

    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_07_DocGet.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 获取文档
            GetRequest getRequest = new GetRequest();
            getRequest.index("user").id("1001");
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);

            // 响应信息
            log.info(getResponse.getSourceAsString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
