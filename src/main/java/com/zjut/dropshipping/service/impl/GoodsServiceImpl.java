package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Goods;
import com.zjut.dropshipping.dto.GoodsDTO;
import com.zjut.dropshipping.dto.PageChunk;
import com.zjut.dropshipping.repository.GoodsRepository;
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

    @Autowired
    public GoodsServiceImpl(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Override
    public ServerResponse getList(String keyword, Integer categoryId,
                                               Integer pageNum, Integer pageSize,
                                               String orderBy) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        Page<Goods> goodsPage = goodsRepository.findByCategoryId(categoryId, pageRequest);
        return ServerResponse.createBySuccess(getPageChunk(goodsPage));
    }

    private PageChunk<GoodsDTO> getPageChunk(Page<Goods> goodsPage) {
        PageChunk<GoodsDTO> pageChunk = new PageChunk<>();
        pageChunk.setContent(getGoodsDTO(goodsPage.getContent()));
        pageChunk.setTotalPages(goodsPage.getTotalPages());
        pageChunk.setTotalElements(goodsPage.getTotalElements());
        pageChunk.setPageNumber(goodsPage.getPageable().getPageNumber() + 1);
        pageChunk.setNumberOfElements(goodsPage.getNumberOfElements());
        return pageChunk;
    }

    private List<GoodsDTO> getGoodsDTO(List<Goods> goodsList) {
        List<GoodsDTO> goodsDTOList = new ArrayList<>();
        for (Goods goods :
                goodsList) {
            GoodsDTO goodsDTO = new GoodsDTO();
            goodsDTO.setGoodsId(goods.getGoodsId());
            goodsDTO.setName(goods.getName());
            goodsDTO.setPrice(goods.getPrice());

            goodsDTOList.add(goodsDTO);
        }
        return goodsDTOList;
    }
}
