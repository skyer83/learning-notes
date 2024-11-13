package com.tanji.es.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 文档 - 批量删除
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_10_DocDeleteBatch {

    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_10_DocDeleteBatch.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 批量创建文档
            BulkRequest bulkRequest = new BulkRequest();

            bulkRequest.add(new DeleteRequest("user").id("1001"));
            bulkRequest.add(new DeleteRequest("user").id("1002"));
            bulkRequest.add(new DeleteRequest("user").id("1003"));

            BulkResponse bulkResponses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

            // 响应信息
            // 消耗时间：100ms
            log.info(bulkResponses.getTook().toString());
            // [{"id":"1001","opType":"DELETE","response":{"shardInfo":{"total":2,"successful":1,"failures":[],"failed":0,"fragment":false},"shardId":{"index":{"name":"user","uuid":"_na_","fragment":false},"id":-1,"indexName":"user","fragment":true},"id":"1001","type":"_doc","version":2,"seqNo":6,"primaryTerm":1,"result":"DELETED","index":"user","fragment":false},"failure":null,"type":"_doc","index":"user","version":2,"failureMessage":null,"failed":false,"itemId":0,"fragment":false},{"id":"1002","opType":"DELETE","response":{"shardInfo":{"total":2,"successful":1,"failures":[],"failed":0,"fragment":false},"shardId":{"index":{"name":"user","uuid":"_na_","fragment":false},"id":-1,"indexName":"user","fragment":true},"id":"1002","type":"_doc","version":2,"seqNo":7,"primaryTerm":1,"result":"DELETED","index":"user","fragment":false},"failure":null,"type":"_doc","index":"user","version":2,"failureMessage":null,"failed":false,"itemId":1,"fragment":false},{"id":"1003","opType":"DELETE","response":{"shardInfo":{"total":2,"successful":1,"failures":[],"failed":0,"fragment":false},"shardId":{"index":{"name":"user","uuid":"_na_","fragment":false},"id":-1,"indexName":"user","fragment":true},"id":"1003","type":"_doc","version":2,"seqNo":8,"primaryTerm":1,"result":"DELETED","index":"user","fragment":false},"failure":null,"type":"_doc","index":"user","version":2,"failureMessage":null,"failed":false,"itemId":2,"fragment":false}]
            log.info(new ObjectMapper().writeValueAsString(bulkResponses.getItems()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
