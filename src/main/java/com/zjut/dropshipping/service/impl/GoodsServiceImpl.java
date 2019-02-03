package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.dataobject.Goods;
import com.zjut.dropshipping.dataobject.GoodsEvaluation;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.dto.*;
import com.zjut.dropshipping.repository.AgentRepository;
import com.zjut.dropshipping.repository.GoodsEvaluationRepository;
import com.zjut.dropshipping.repository.GoodsRepository;
import com.zjut.dropshipping.repository.ProducerRepository;
import com.zjut.dropshipping.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository,
                            GoodsEvaluationRepository goodsEvaluationRepository,
                            AgentRepository agentRepository,
                            ProducerRepository producerRepository) {
        this.goodsRepository = goodsRepository;
        this.agentRepository = agentRepository;
        this.goodsEvaluationRepository = goodsEvaluationRepository;
        this.producerRepository = producerRepository;
    }

    @Override
    public ServerResponse getList(String keyword, Integer categoryId,
                                               Integer pageNum, Integer pageSize,
                                               String orderBy) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        Page<Goods> goodsPage = goodsRepository.findByCategoryId(categoryId, pageRequest);
        return ServerResponse.createBySuccess(getPageChunk(goodsPage));
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
    public ServerResponse<Double> getAverageLevel(Integer goodsId) {
        Double averageLevel = goodsEvaluationRepository.findAverageLevelByGoodsId(goodsId);
        if (averageLevel == null) {
            return ServerResponse.createBySuccess(0.00);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return ServerResponse.createBySuccess(Double.parseDouble(decimalFormat.format(averageLevel)));
    }

    @Override
    public ServerResponse getEvaluation(Integer goodsId) {
        List<GoodsEvaluation> goodsEvaluationList = goodsEvaluationRepository.findByGoodsId(goodsId);
        if (goodsEvaluationList.size() == 0) {
            return ServerResponse.createByErrorMessage("该商品暂无评论");
        }
        return ServerResponse.createBySuccess(getEvaluationList(goodsEvaluationList));
    }

    @Override
    public ServerResponse getProducer(Integer goodsId) {
        Goods goods = goodsRepository.findByGoodsId(goodsId);
        Producer producer = producerRepository.findOneById(goods.getProducerId());
        if (producer == null) {
            return ServerResponse.createByErrorMessage("找不到该商品的厂商信息");
        }
        producer.setContactIdentityNumber(null);
        producer.setLicenseNumber(null);
        producer.setStatus(null);
        producer.setJoinTime(null);
        producer.setCredibility(null);

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
        pageChunk.setContent(getGoodsList(goodsPage.getContent()));
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
            goods.setStatus(null);
            goods.setCreateTime(null);
            goods.setUpdateTime(null);
        }
        return goodsList;
    }
}
