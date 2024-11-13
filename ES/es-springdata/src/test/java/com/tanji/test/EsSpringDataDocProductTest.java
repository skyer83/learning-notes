package com.tanji.test;

import com.tanji.test.dao.ProductDao;
import com.tanji.test.pojo.po.ProductPO;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档操作
 * @author shenjh
 * @version 1.0
 * @since 2024-10-11 13:42
 */
@RunWith(SpringRunner.class) // 表明Test测试类要使用注入的类，比如@Autowired注入的类，有了@RunWith(SpringRunner.class)这些类才能实例化到spring容器中，自动注入才能生效
@SpringBootTest
public class EsSpringDataDocProductTest {

    @Autowired
    private ProductDao productDao;

    /**
     * 新增文档
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:31
     */
    @Test
    public void create() {
        ProductPO productPO = new ProductPO();
        productPO.setId(1L);
        productPO.setTitle("华为手机");
        productPO.setCategory("手机");
        productPO.setPrice(2999.0);
        productPO.setImages("http://www.atguigu/hw.jpg");
        productDao.save(productPO);
    }

    /**
     * 新增文档
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:31
     */
    @Test
    public void update() {
        ProductPO productPO = new ProductPO();
        productPO.setId(1L);
        productPO.setTitle("小米手机");
        productPO.setCategory("手机");
        productPO.setPrice(9999.0);
        productPO.setImages("http://www.atguigu/xm.jpg");
        // 也是使用 save ，存在相同 ID 时，进行更新
        productDao.save(productPO);
    }

    /**
     * 根据 ID 查询文档
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:35
     */
    @Test
    public void findById() {
        ProductPO productPO = productDao.findById(1L).get();
        System.out.println(productPO);
    }

    /**
     * 查询 product 的所有数据
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:36
     */
    @Test
    public void findAll() {
        Iterable<ProductPO> products = productDao.findAll();
        for (ProductPO productPO : products) {
            System.out.println(productPO);
        }
    }

    /**
     * 删除文档
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:38
     */
    @Test
    public void delete() {
        ProductPO productPO = new ProductPO();
        productPO.setId(1L);
        productDao.delete(productPO);
    }

    /**
     * 批量新增
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:40
     */
    @Test
    public void saveAll() {
        List<ProductPO> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductPO product = new ProductPO();
            product.setId(Long.valueOf(i));
            product.setTitle("["+i+"]小米手机");
            product.setCategory("手机");
            product.setPrice(1999.0+i);
            product.setImages("http://www.atguigu/xm.jpg");
            productList.add(product);
        }
        productDao.saveAll(productList);
    }

    /**
     * 分页查询
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:41
     */
    @Test
    public void findByPageable() {
        // 设置排序(排序方式，正序还是倒序，排序的 id)
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        // 当前页，第一页从 0 开始，1 表示第二页
        int currPage = 0;
        // 每页显示多少条
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(currPage, pageSize, sort);
        Page<ProductPO> productPage = productDao.findAll(pageRequest);
        List<ProductPO> productList = productPage.getContent();
        for (ProductPO productPO : productList) {
            System.out.println(productPO);
        }
    }

    /**
     * term 查询，根据参数查询构建器对象调用搜索方法 search(termQueryBuilder)
     *
     * @return void
     * @author shenjh
     * @since 2024/10/11 14:53
     */
    @Test
    public void termQuery() {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");
        Iterable<ProductPO> products = productDao.search(termQueryBuilder);
        for (ProductPO po : products) {
            System.out.println(po);
        }
    }

    @Test
    public void termQueryByPage() {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");

        // 设置排序(排序方式，正序还是倒序，排序的 id)
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        // 当前页，第一页从 0 开始，1 表示第二页
        int currPage = 0;
        // 每页显示多少条
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(currPage, pageSize, sort);

        Page<ProductPO> productPage = productDao.search(termQueryBuilder, pageRequest);
        List<ProductPO> products = productPage.getContent();
        for (ProductPO po : products) {
            System.out.println(po);
        }
    }
}
