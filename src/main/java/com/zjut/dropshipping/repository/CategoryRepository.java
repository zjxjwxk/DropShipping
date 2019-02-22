package com.zjut.dropshipping.repository;

import com.zjut.dropshipping.dataobject.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zjxjwxk
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findOneById(Integer categoryId);

    List<Category> findByParentId(Integer parentId);

    @Query(value="SELECT name from category where id in (select category_id FROM goods WHERE goods_id in ( select goods_id from order_item where order_id in ( SELECT order_id FROM `order` WHERE agent_id = ?1) ))", nativeQuery = true)
    List<String> findProductSaleListByAgentId(Integer agentId);
}
