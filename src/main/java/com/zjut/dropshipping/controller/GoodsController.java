package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zjxjwxk
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    @Autowired
    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/get_list")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                               @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                               @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return goodsService.getList(keyword, categoryId, pageNum, pageSize, orderBy);
    }

    @GetMapping("/get_detail")
    @ResponseBody
    public ServerResponse getDetail(Integer goodsId) {
        return goodsService.getDetail(goodsId);
    }

    @GetMapping("/get_evaluation")
    @ResponseBody
    public ServerResponse getEvaluation(Integer goodsId) {
        return goodsService.getEvaluation(goodsId);
    }

    @GetMapping("/get_producer")
    @ResponseBody
    public ServerResponse getProducer(Integer goodsId) {
        return goodsService.getProducer(goodsId);
    }

    @GetMapping("/get_average_level")
    @ResponseBody
    public ServerResponse getAverageLevel(Integer goodsId) {
        return goodsService.getAverageLevel(goodsId);
    }
}
