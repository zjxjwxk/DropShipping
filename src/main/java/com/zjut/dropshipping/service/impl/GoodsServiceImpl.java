package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.*;
import com.zjut.dropshipping.dto.*;
import com.zjut.dropshipping.repository.*;
import com.zjut.dropshipping.service.CategoryService;
import com.zjut.dropshipping.service.ExchangeRateService;
import com.zjut.dropshipping.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final AgreementRepository agreementRepository;
    private final OrderItemRepository orderItemRepository;
    private final SpecificationRepository specificationRepository;
    private final GoodsSpecItemRepository goodsSpecItemRepository;

    private final CategoryService categoryService;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository,
                            GoodsEvaluationRepository goodsEvaluationRepository,
                            AgentRepository agentRepository,
                            ProducerRepository producerRepository,
                            AgreementRepository agreementRepository,
                            CategoryService categoryService,
                            OrderItemRepository orderItemRepository,
                            SpecificationRepository specificationRepository,
                            GoodsSpecItemRepository goodsSpecItemRepository,
                            ExchangeRateService exchangeRateService) {
        this.goodsRepository = goodsRepository;
        this.agentRepository = agentRepository;
        this.goodsEvaluationRepository = goodsEvaluationRepository;
        this.producerRepository = producerRepository;
        this.agreementRepository = agreementRepository;
        this.categoryService = categoryService;
        this.orderItemRepository = orderItemRepository;
        this.specificationRepository = specificationRepository;
        this.goodsSpecItemRepository = goodsSpecItemRepository;
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public ServerResponse getList(String country, String keyword, Integer categoryId, Integer producerId,
                                  Integer agreementAgentId, Integer pageNum, Integer pageSize,
                                  String orderBy) {
        List<Integer> categoryIdList = categoryService.getCategoryAndChildrenIdListByParentId(categoryId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Goods> goodsPage;
        if (producerId != null) {
            goodsPage = goodsRepository.findByProducerIdAndCategoryIdIn(producerId, categoryIdList, pageable);
        } else if (agreementAgentId != null) {
            List<Integer> producerIdList = agreementRepository.findProducerIdListByAgentIdAndState(agreementAgentId, Const.AgreementState.NORMAL);
            goodsPage = goodsRepository.findByProducerIdInAndCategoryIdIn(producerIdList, categoryIdList, pageable);
        } else {
            goodsPage = goodsRepository.findByCategoryIdIn(categoryIdList, pageable);
        }

        return ServerResponse.createBySuccess(this.getPageChunk(country, goodsPage));
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

    @Override
    public ServerResponse getSpecification(Integer goodsId) {
        List<GoodsSpecItem> goodsSpecItemList = goodsSpecItemRepository.findByGoodsId(goodsId);
        List<SpecificationDTO> specificationDTOList = new ArrayList<>();
        if (goodsSpecItemList.size() == 0) {
            return ServerResponse.createByErrorMessage("该商品不存在");
        } else {
            for (GoodsSpecItem goodsSpecItem:
                    goodsSpecItemList) {
                Specification specification = specificationRepository.findBySpecId(goodsSpecItem.getSpecId());
                SpecificationDTO specificationDTO = new SpecificationDTO(goodsSpecItem.getGoodsSpecId(),
                        specification.getName(), specification.getValue(),
                        goodsSpecItem.getPriceDifference());
                specificationDTOList.add(specificationDTO);
            }
        }
        return ServerResponse.createBySuccess(specificationDTOList);
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

    private PageChunk<GoodsListDTO> getPageChunk(String country, Page<Goods> goodsPage) {
        PageChunk<GoodsListDTO> pageChunk = new PageChunk<>();
        pageChunk.setContent(this.getGoodsListDTOList(country, goodsPage.getContent()));
        pageChunk.setTotalPages(goodsPage.getTotalPages());
        pageChunk.setTotalElements(goodsPage.getTotalElements());
        pageChunk.setPageNumber(goodsPage.getPageable().getPageNumber() + 1);
        pageChunk.setNumberOfElements(goodsPage.getNumberOfElements());
        return pageChunk;
    }

    private List<GoodsListDTO> getGoodsListDTOList(String country, List<Goods> goodsList) {
        List<GoodsListDTO> goodsListDTOList = new ArrayList<>();
        for (Goods goods :
                goodsList) {
            GoodsListDTO goodsListDTO = new GoodsListDTO();
            goodsListDTO.setGoodsId(goods.getGoodsId());
            goodsListDTO.setName(goods.getName());
            goodsListDTO.setPrice(exchangeRateService.getExchangePrice(country, goods.getPrice()));
            Producer producer = producerRepository.findOneById(goods.getProducerId());
            goodsListDTO.setProducerId(producer.getId());
            goodsListDTO.setProducerName(producer.getName());
            goodsListDTOList.add(goodsListDTO);
        }
        return goodsListDTOList;
    }

    @Override
    public ServerResponse addGoodsModel(Integer goodsId, String name, String value , Double price){
        GoodsSpecItem goodsSpecItem = new GoodsSpecItem();
        Specification specification = new Specification();
        if(specificationRepository.findByNameAndValue(name,value) == null ){
            specification.setName(name);
            specification.setValue(value);
            specificationRepository.save(specification);
        }
        goodsSpecItem.setPriceDifference(price-goodsRepository.findPriceByGoodId(goodsId));
        goodsSpecItem.setGoodsId(goodsId);
        goodsSpecItem.setSpecId(specificationRepository.findSpeId(name,value));
        goodsSpecItemRepository.save(goodsSpecItem);
        return ServerResponse.createBySuccessMessage("添加成功");
    }
}
