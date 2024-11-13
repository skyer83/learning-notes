package com.tanji.es.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 文档 - 批量新增
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_09_DocInsertBatch {

    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_09_DocInsertBatch.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 批量创建文档
            BulkRequest bulkRequest = new BulkRequest();

            bulkRequest.add(new IndexRequest("user").id("1001").source(XContentType.JSON, "name", "zhangsan"));
            bulkRequest.add(new IndexRequest("user").id("1002").source(XContentType.JSON, "name", "lisi"));
            bulkRequest.add(new IndexRequest("user").id("1003").source(XContentType.JSON, "name", "wangwu"));

            BulkResponse bulkResponses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            // 响应信息
            // 消耗时间:104ms
            log.info(bulkResponses.getTook().toString());
            // [{"id":"1001","opType":"INDEX","response":{"shardInfo":{"total":2,"successful":1,"failures":[],"failed":0,"fragment":false},"shardId":{"index":{"name":"user","uuid":"_na_","fragment":false},"id":-1,"indexName":"user","fragment":true},"id":"1001","type":"_doc","version":1,"seqNo":9,"primaryTerm":1,"result":"CREATED","index":"user","fragment":false},"failure":null,"type":"_doc","index":"user","version":1,"itemId":0,"failed":false,"failureMessage":null,"fragment":false},{"id":"1002","opType":"INDEX","response":{"shardInfo":{"total":2,"successful":1,"failures":[],"failed":0,"fragment":false},"shardId":{"index":{"name":"user","uuid":"_na_","fragment":false},"id":-1,"indexName":"user","fragment":true},"id":"1002","type":"_doc","version":1,"seqNo":10,"primaryTerm":1,"result":"CREATED","index":"user","fragment":false},"failure":null,"type":"_doc","index":"user","version":1,"itemId":1,"failed":false,"failureMessage":null,"fragment":false},{"id":"1003","opType":"INDEX","response":{"shardInfo":{"total":2,"successful":1,"failures":[],"failed":0,"fragment":false},"shardId":{"index":{"name":"user","uuid":"_na_","fragment":false},"id":-1,"indexName":"user","fragment":true},"id":"1003","type":"_doc","version":1,"seqNo":11,"primaryTerm":1,"result":"CREATED","index":"user","fragment":false},"failure":null,"type":"_doc","index":"user","version":1,"itemId":2,"failed":false,"failureMessage":null,"fragment":false}]
            log.info(new ObjectMapper().writeValueAsString(bulkResponses.getItems()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
