package com.tanji.es.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 文档 - 新增
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_05_DocInsert {

    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_05_DocInsert.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 创建文档
            IndexRequest indexRequest = new IndexRequest("user");
            indexRequest.id("1001");

            User user = new User();
            user.setName("zhangsan");
            user.setAge(18);
            user.setSex("男");

            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(user);
            indexRequest.source(userJson, XContentType.JSON);

            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

            // 响应信息
            log.info(indexResponse.getResult().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
