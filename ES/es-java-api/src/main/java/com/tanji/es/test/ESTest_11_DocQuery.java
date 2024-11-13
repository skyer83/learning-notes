package com.tanji.es.test;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 文档 - 高级查询
 * @author shenjh
 * @version 1.0
 * @since 2024-09-27 17:16
 */
public class ESTest_11_DocQuery {

    // 套壳使用 slf4j
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ESTest_11_DocQuery.class);

    public static void main(String[] args) {
        // 创建 ES 客户端
        RestClientBuilder restClientBuilder = TestUtils.buildRestClientBuilder();
        // 自动关闭 ES 客户端
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder)) {
            // 批量新增文档
//            docInsertBatch(restHighLevelClient);

            // 1、全量查询
//            matchAllQuery(restHighLevelClient);
            // 2、条件查询
//            termQuery(restHighLevelClient);
            // 3、分页查询
//            pageQuery(restHighLevelClient);
            // 4、排序查询
//            sortQuery(restHighLevelClient);
            // 5、过滤字段（指定要获取的字段）
//            fetchSource(restHighLevelClient);
            // 6、组合查询
//            boolQuery(restHighLevelClient);
            // 7、范围查询
//            rangeQuery(restHighLevelClient);
            // 8、模糊查询
//            fuzzyQuery(restHighLevelClient);
            // 9、高亮显示
//            highlightQuery(restHighLevelClient);
            // 10、聚合查询（如：max、min、avg）
//            aggrgationQuery(restHighLevelClient);
            // 11、分组查询（聚合查询）
            aggrgationTermQuery(restHighLevelClient);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分组查询（聚合查询）
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void aggrgationTermQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");
        searchSourceBuilder.aggregation(termsAggregationBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
        // "aggregations":{"lterms#ageGroup":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":31,"doc_count":8},{"key":81,"doc_count":5},{"key":18,"doc_count":4},{"key":20,"doc_count":1},{"key":21,"doc_count":1}]}}
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            // {"name":"ageGroup","metadata":null,"buckets":[{"aggregations":{"asMap":{},"fragment":true},"keyAsString":"31","docCount":8,"docCountError":0,"key":31,"keyAsNumber":31,"fragment":true},{"aggregations":{"asMap":{},"fragment":true},"keyAsString":"81","docCount":5,"docCountError":0,"key":81,"keyAsNumber":81,"fragment":true},{"aggregations":{"asMap":{},"fragment":true},"keyAsString":"18","docCount":4,"docCountError":0,"key":18,"keyAsNumber":18,"fragment":true},{"aggregations":{"asMap":{},"fragment":true},"keyAsString":"20","docCount":1,"docCountError":0,"key":20,"keyAsNumber":20,"fragment":true},{"aggregations":{"asMap":{},"fragment":true},"keyAsString":"21","docCount":1,"docCountError":0,"key":21,"keyAsNumber":21,"fragment":true}],"type":"lterms","docCountError":0,"sumOfOtherDocCounts":0,"fragment":true}
            log.info(TestUtils.toJsonStr(aggregation));
        }
    }

    /**
     * 聚合查询（如：max、min、avg）
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void aggrgationQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders.max("maxAge").field("age");
        searchSourceBuilder.aggregation(maxAggregationBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
        // "aggregations":{"max#maxAge":{"value":81.0}}
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            // {"name":"maxAge","metadata":null,"value":81.0,"valueAsString":"81.0","type":"max","fragment":true}
            log.info(TestUtils.toJsonStr(aggregation));
        }
    }

    /**
     * 高亮显示查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void highlightQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "zhangshan");
        searchSourceBuilder.query(termQueryBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 前缀
        highlightBuilder.preTags("<font color='red'>");
        // 后缀
        highlightBuilder.postTags("</font>");
        // 高亮字段
        highlightBuilder.field("name");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // "hits":[{"_index":"user","_type":"_doc","_id":"1001","_score":2.5902672,"_source":{"name":"zhangshan","age":18,"sex":"男"},"highlight":{"name":["<font color='red'>zhangshan</font>"]}}]
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            // {"name":"zhangshan","age":18,"sex":"男"}
            log.info(searchHit.getSourceAsString());
            // {name=[name], fragments[[<font color='red'>zhangshan</font>]]}
            log.info(searchHit.getHighlightFields().toString());
        }
    }

    /**
     * 模糊查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void fuzzyQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "wangwu");

        /*
            https://blog.csdn.net/UbuntuTouch/article/details/101287399
            在实际的搜索中，我们有时候会打错字，从而导致搜索不到。在 Elasticsearch 中，我们可以使用 fuzziness 属性来进行模糊查询，从而达到搜索有错别字的情形。
            match 查询具有 “fuziness” 属性。它可以被设置为 “0”， “1”， “2”或 “auto”。“auto” 是推荐的选项，它会根据查询词的长度定义距离。在实际的使用中，
            当我们使用 auto 时，如果字符串的长度大于5，那么 funziness 的值自动设置为2，如果字符串的长度小于2，那么 fuziness 的值自动设置为 0。

            返回包含与搜索词相似的词的文档，以 Levenshtein 编辑距离测量。
            编辑距离是将一个术语转换为另一个术语所需的一个字符更改的次数。 这些更改可以包括：
                更改字符（box→fox）
                删除字符（black→lack）
                插入字符（sic→sick）
                转置两个相邻字符（act→cat）
         */
        /*
            https://cloud.tencent.com/developer/article/1797423
            fuzzy中有个编辑距离的概念，编辑距离是对两个字符串差异长度的量化，及一个字符至少需要处理多少次才能变成另一个字符，
            比如lucene和lucece只差了一个字符他们的编辑距离是1，当设置编辑距离为 1 时，编辑距离为 0 的也会被查询出来，但编辑距离为 2 的则不会。
            fuzziness参数，可以赋值为0，1，2和AUTO，默认是AUTO，AUTO的意思是，根据查询的字符串长度决定允许的编辑距离，规则是：
                0..2 完全匹配（就是不允许模糊）
                3..5 编辑距离是1
                大于5 编辑距离是2
         */
        /*
            {"name":"wangwu","age":81,"sex":"男"}
         */
//        fuzzyQueryBuilder.fuzziness(Fuzziness.ZERO);

        /*
            0 - {"name":"wangwu","age":81,"sex":"男"}
            1 - {"name":"wangwu1","age":81,"sex":"男"}
            1 - {"name":"wang2wu","age":81,"sex":"男"}
            1 - {"name":"wanguw","age":81,"sex":"男"}
         */
//        fuzzyQueryBuilder.fuzziness(Fuzziness.ONE);

        /*
            0 - {"name":"wangwu","age":81,"sex":"男"}
            1 - {"name":"wangwu1","age":81,"sex":"男"}
            1 - {"name":"wang2wu","age":81,"sex":"男"}
            1 - {"name":"wanguw","age":81,"sex":"男"}
            2 - {"name":"wangwu21","age":21,"sex":"男"}
            2 - {"name":"wangwu31","age":31,"sex":"男"}
            2 - {"name":"wang2wu3","age":31,"sex":"男"}
            2 - {"name":"wanwgu9","age":31,"sex":"男"}
            2 - {"name":"wnawgu","age":31,"sex":"男"}

            nugwaw、nwawgu 编辑距离超过 2
         */
        fuzzyQueryBuilder.fuzziness(Fuzziness.TWO);

        searchSourceBuilder.query(fuzzyQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    /**
     * 范围查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void rangeQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");

        // 大于等于
        rangeQueryBuilder.gte(18);
        // 小于等于
        rangeQueryBuilder.lte(20);

        searchSourceBuilder.query(rangeQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    /**
     * 组合查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void boolQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 必须满足（类似 and）
//        boolQueryBuilder.must(QueryBuilders.matchQuery("age", 18));
////        boolQueryBuilder.must(QueryBuilders.matchQuery("sex", "女"));
//        boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex", "男"));

        // 或者满足（类似 or）
        boolQueryBuilder.should(QueryBuilders.matchQuery("age", 18));
        boolQueryBuilder.should(QueryBuilders.matchQuery("age", 20));


        searchSourceBuilder.query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    /**
     * 过滤字段（指定要获取的字段）
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void fetchSource(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());

        // 有明确指定 excludes 以 excludes 未准，未明确指定 excludes 的以 includes 未准
        String[] includes = {"name"};
        String[] excludes = {"age"};
        searchSourceBuilder.fetchSource(includes, excludes);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    /**
     * 排序查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void sortQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.sort("age", SortOrder.DESC);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    /**
     * 分页查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void pageQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());

        // 起始位置计算公式：（页码 - 1）* 每页条数，如取第 3 页数据，每页取 2 条数据：（3 - 1） * 2 = 4
        searchSourceBuilder.from(4);
        // 每页取 2 条数据
        searchSourceBuilder.size(2);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    /**
     * 条件查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void termQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age", 18)));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    /**
     * 全量查询
     *
     * @param restHighLevelClient
     * @return void
     * @author shenjh
     * @since 2024/10/7 17:01
     */
    private static void matchAllQuery(RestHighLevelClient restHighLevelClient) throws IOException {
        SearchRequest searchRequest = new SearchRequest("user");
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();

        // 消耗时间
        log.info(searchResponse.getTook().toString());
        // 命中记录数
        log.info(searchHits.getTotalHits().toString());
        for (SearchHit searchHit : searchHits) {
            log.info(searchHit.getSourceAsString());
        }
    }

    private static void docInsertBatch(RestHighLevelClient restHighLevelClient) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();

        // 注意：当 id 相同时，会进行更新，而不是新增
        bulkRequest.add(new IndexRequest("user").id("1001").source(XContentType.JSON, "name", "zhangshan", "age", 18, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1002").source(XContentType.JSON, "name", "lisi", "age", 18, "sex", "女"));
        bulkRequest.add(new IndexRequest("user").id("1003").source(XContentType.JSON, "name", "wangwu", "age", 81, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1004").source(XContentType.JSON, "name", "zhaoliu", "age", 18, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1005").source(XContentType.JSON, "name", "feiqi", "age", 20, "sex", "女"));
        bulkRequest.add(new IndexRequest("user").id("1006").source(XContentType.JSON, "name", "wuba", "age", 18, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1007").source(XContentType.JSON, "name", "wangwu21", "age", 21, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1008").source(XContentType.JSON, "name", "wangwu31", "age", 31, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1009").source(XContentType.JSON, "name", "wang2wu3", "age", 31, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1010").source(XContentType.JSON, "name", "wangwu333", "age", 31, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1011").source(XContentType.JSON, "name", "wanwgu31", "age", 31, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1012").source(XContentType.JSON, "name", "wanwgu32", "age", 31, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1013").source(XContentType.JSON, "name", "wanwgu9", "age", 31, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1015").source(XContentType.JSON, "name", "wangwu1", "age", 81, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1016").source(XContentType.JSON, "name", "wang2wu", "age", 81, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1017").source(XContentType.JSON, "name", "wanguw", "age", 81, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1014").source(XContentType.JSON, "name", "wnawgu", "age", 31, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1018").source(XContentType.JSON, "name", "nugwaw", "age", 81, "sex", "男"));
        bulkRequest.add(new IndexRequest("user").id("1019").source(XContentType.JSON, "name", "nwawgu", "age", 31, "sex", "男"));

        restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

}
