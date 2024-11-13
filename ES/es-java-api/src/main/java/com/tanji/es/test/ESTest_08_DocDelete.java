package com.tanji.es.test;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 文档 - 删除
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_08_DocDelete {

    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_08_DocDelete.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 删除文档
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.index("user").id("1001");
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);

            // 响应信息
            // DeleteResponse[index=user,type=_doc,id=1001,version=3,result=deleted,shards=ShardInfo{total=2, successful=1, failures=[]}]
            log.info(deleteResponse.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
