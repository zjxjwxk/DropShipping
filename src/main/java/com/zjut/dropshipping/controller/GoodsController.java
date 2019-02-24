package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
                                  @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId,
                                  @RequestParam(value = "inAgreement", defaultValue = "false") Boolean inAgreement,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "orderBy", defaultValue = "amount") String orderBy,
                                  HttpSession session) {
        if (inAgreement) {
            Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
            if (agent == null) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
            }
            return goodsService.getList(keyword, categoryId, agent.getId(), pageNum, pageSize, orderBy);
        }
        return goodsService.getList(keyword, categoryId, null, pageNum, pageSize, orderBy);
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

    @GetMapping("/get_sales_volume")
    @ResponseBody
    public ServerResponse getSalesVolume(Integer goodsId) {
        return goodsService.getSalesVolume(goodsId);
    }

    @GetMapping("/get_specification")
    @ResponseBody
    public ServerResponse getSpecification(Integer goodsId) {
        return goodsService.getSpecification(goodsId);
    }

    @PostMapping("/add_goods_model")
    @ResponseBody
    public ServerResponse addGoodsModel(Integer goodsId, String name, String value, Double price) {

        return goodsService.addGoodsModel(goodsId, name, value, price);
    }
}
