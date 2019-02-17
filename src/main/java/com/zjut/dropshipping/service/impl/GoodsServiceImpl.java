package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.*;
import com.zjut.dropshipping.dto.*;
import com.zjut.dropshipping.repository.*;
import com.zjut.dropshipping.service.CategoryService;
import com.zjut.dropshipping.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjxjwxk
 */
@Service("GoodsService")
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsEvaluationRepository goodsEvaluationRepository;
    private final AgentRepository agentRepository;
    private final ProducerRepository producerRepository;
    private final OrderRepository orderRepository;
    private final AgreementRepository agreementRepository;
    private final OrderItemRepository orderItemRepository;

    private final CategoryService categoryService;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository,
                            GoodsEvaluationRepository goodsEvaluationRepository,
                            AgentRepository agentRepository,
                            ProducerRepository producerRepository,
                            OrderRepository orderRepository,
                            AgreementRepository agreementRepository,
                            CategoryService categoryService,
                            OrderItemRepository orderItemRepository) {
        this.goodsRepository = goodsRepository;
        this.agentRepository = agentRepository;
        this.goodsEvaluationRepository = goodsEvaluationRepository;
        this.producerRepository = producerRepository;
        this.orderRepository = orderRepository;
        this.agreementRepository = agreementRepository;
        this.categoryService = categoryService;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public ServerResponse getList(String keyword, Integer categoryId, Integer agreementAgentId, Integer pageNum, Integer pageSize,
                                  String orderBy) {
        List<Integer> categoryIdList = categoryService.getCategoryAndChildrenIdListByParentId(categoryId);
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        Page<Goods> goodsPage;
        if (agreementAgentId != null) {
            List<Integer> producerIdList = agreementRepository.findProducerIdListByAgentIdAndState(agreementAgentId, Const.AgreementState.NORMAL);
            goodsPage = goodsRepository.findByProducerIdInAndCategoryIdIn(producerIdList, categoryIdList, pageRequest);
        } else {
            goodsPage = goodsRepository.findByCategoryIdIn(categoryIdList, pageRequest);
        }

        return ServerResponse.createBySuccess(this.getPageChunk(goodsPage));
    }

    @Override
    public ServerResponse getDetail(Integer goodsId) {

        Goods goods = goodsRepository.findByGoodsId(goodsId);

        if (goods == null) {
            return ServerResponse.createByErrorMessage("该商品不存在");
        }

        goods.setCreateTime(null);
        goods.setUpdateTime(null);

        return ServerResponse.createBySuccess(goods);
    }

    @Override
    public ServerResponse<Integer> getSalesVolume(Integer goodsId) {
        Integer salesVolume = orderItemRepository.findTotalAmountByGoodsId(goodsId);
        return ServerResponse.createBySuccess(salesVolume);
    }

    @Override
    public ServerResponse getEvaluation(Integer goodsId) {
        List<GoodsEvaluation> goodsEvaluationList = goodsEvaluationRepository.findByGoodsId(goodsId);
        if (goodsEvaluationList.size() == 0) {
            return ServerResponse.createByErrorMessage("该商品暂无评论");
        }
        return ServerResponse.createBySuccess(this.getEvaluationList(goodsEvaluationList));
    }

    @Override
    public ServerResponse getProducer(Integer goodsId) {
        Goods goods = goodsRepository.findByGoodsId(goodsId);
        Producer producer = producerRepository.findOneById(goods.getProducerId());
        if (producer == null) {
            return ServerResponse.createByErrorMessage("找不到该商品的厂商信息");
        }
        producer.setNull();

        return ServerResponse.createBySuccess(producer);
    }

    private List<GoodsEvaluationDTO> getEvaluationList(List<GoodsEvaluation> goodsEvaluationList) {
        List<GoodsEvaluationDTO> goodsEvaluationDTOList = new ArrayList<>();
        for (GoodsEvaluation goodsEvaluation:
                goodsEvaluationList) {
            GoodsEvaluationDTO goodsEvaluationDTO = new GoodsEvaluationDTO();
            Agent agent = agentRepository.findOneById(goodsEvaluation.getAgentId());

            goodsEvaluationDTO.setAgentId(goodsEvaluation.getAgentId());
            goodsEvaluationDTO.setAgentName(agent.getName());
            goodsEvaluationDTO.setLevel(goodsEvaluation.getLevel());
            goodsEvaluationDTO.setContent(goodsEvaluation.getContent());

            goodsEvaluationDTOList.add(goodsEvaluationDTO);
        }
        return goodsEvaluationDTOList;
    }

    private PageChunk<Goods> getPageChunk(Page<Goods> goodsPage) {
        PageChunk<Goods> pageChunk = new PageChunk<>();
        pageChunk.setContent(this.getGoodsList(goodsPage.getContent()));
        pageChunk.setTotalPages(goodsPage.getTotalPages());
        pageChunk.setTotalElements(goodsPage.getTotalElements());
        pageChunk.setPageNumber(goodsPage.getPageable().getPageNumber() + 1);
        pageChunk.setNumberOfElements(goodsPage.getNumberOfElements());
        return pageChunk;
    }

    private List<Goods> getGoodsList(List<Goods> goodsList) {
        for (Goods goods :
                goodsList) {
            goods.setProducerId(null);
            goods.setCategoryId(null);
            goods.setStock(null);
            goods.setState(null);
            goods.setCreateTime(null);
            goods.setUpdateTime(null);
        }
        return goodsList;
    }
}
