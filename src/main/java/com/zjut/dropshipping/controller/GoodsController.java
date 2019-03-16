package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.dataobject.Producer;
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
                                  @RequestParam(value = "producerId", required = false) Integer producerId,
                                  @RequestParam(value = "inAgreement", defaultValue = "false") Boolean inAgreement,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "orderBy", defaultValue = "amount") String orderBy,
                                  HttpSession session) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (inAgreement) {
            if (agent == null) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
            } else {
                return goodsService.getList(agent.getRegion(), keyword, categoryId, producerId, agent.getId(), pageNum, pageSize, orderBy);
            }
        } else if (agent != null) {
            return ServerResponse.createBySuccess(goodsService.getList(agent.getRegion(), keyword, categoryId, producerId, null, pageNum, pageSize, orderBy));
        } else if (producer != null) {
            return ServerResponse.createBySuccess(goodsService.getList(producer.getRegion(), keyword, categoryId, producerId, null, pageNum, pageSize, orderBy));
        } else {
            return goodsService.getList(null, keyword, categoryId, producerId, null, pageNum, pageSize, orderBy);
        }
    }

    @GetMapping("/get_detail")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer goodsId) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (agent != null) {
            return goodsService.getDetail(agent.getRegion(), goodsId);
        } else if (producer != null) {
            return goodsService.getDetail(producer.getRegion(), goodsId);
        }
        return goodsService.getDetail(null, goodsId);
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
    public ServerResponse getSpecification(HttpSession session, Integer goodsId) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (agent != null) {
            return goodsService.getSpecification(agent.getRegion(), goodsId);
        } else if (producer != null) {
            return goodsService.getSpecification(producer.getRegion(), goodsId);
        } else {
            return goodsService.getSpecification(null, goodsId);
        }
    }

    @PostMapping("/add_goods_model")
    @ResponseBody
    public ServerResponse addGoodsModel(Integer goodsId, String name, String value, Double price) {

        return goodsService.addGoodsModel(goodsId, name, value, price);
    }
}
