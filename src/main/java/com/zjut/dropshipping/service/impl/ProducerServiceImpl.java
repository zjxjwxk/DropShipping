package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.dto.PageChunk;
import com.zjut.dropshipping.dto.RecommendProducerDTO;
import com.zjut.dropshipping.repository.ProducerRepository;
import com.zjut.dropshipping.service.ProducerService;
import com.zjut.dropshipping.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ljx
 */
@Service("ProducerService")
public class ProducerServiceImpl implements ProducerService {
    private final ProducerRepository producerRepository;

    @Autowired
    public ProducerServiceImpl(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    @Override
    public ServerResponse<String> register(Producer producer) {

        // 检验厂商名
        ServerResponse<String> validResponse = this.checkValid(producer.getName(), Const.PRODUCER_NAME);
        if (validResponse.isError()) {
            return validResponse;
        }

        // 校验电话号码
        validResponse = this.checkValid(producer.getContactPhone(), Const.PHONE);
        if (validResponse.isError()) {
            return validResponse;
        }

        // 校验身份证号
        validResponse = this.checkValid(producer.getContactIdentityNumber(), Const.IDENTITY_NUMBER);
        if (validResponse.isError()) {
            return validResponse;
        }

        // 检验营业执照
        validResponse = this.checkValid(producer.getLicenseNumber(), Const.LICENSE_NUMBER);
        if (validResponse.isError()) {
            return validResponse;
        }

        // 设置状态为未审核
        producer.setState(Const.AccountState.UNREVIEWED);

        // MD5加密
        producer.setPassword(MD5Util.MD5EncodeUtf8(producer.getPassword()));

        producerRepository.save(producer);

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<Producer> login(String contactPhone, String password) {
        int resultCount = producerRepository.countByContactPhone(contactPhone);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("该手机号码未被注册");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        Producer producer = producerRepository.findByContactPhoneAndPassword(contactPhone, md5Password);
        if (producer == null) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }

        producer.setPassword(null);
        return ServerResponse.createBySuccess("登陆成功", producer);
    }

    private ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 开始校验
            if (Const.PRODUCER_NAME.equals(type)) {
                int resultCount = producerRepository.countByName(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该厂商名已被注册");
                }
            }
            if (Const.PHONE.equals(type)) {
                int resultCount = producerRepository.countByContactPhone(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该手机已被注册");
                }
            }
            if (Const.IDENTITY_NUMBER.equals(type)) {
                int resultCount = producerRepository.countByContactIdentityNumber(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该身份证已被注册");
                }
            }
            if (Const.LICENSE_NUMBER.endsWith(type)) {
                int resultCount = producerRepository.countByLicenseNumber(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该营业执照已被注册");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse  getRecommendProducer(Integer producerId, Integer pageNumber, Integer numberOfElements){
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, numberOfElements);
        Page<Producer> producerPage = producerRepository.findAll(pageRequest);
        return ServerResponse.createBySuccess(getPageChunk(producerPage));
    }

    private PageChunk<RecommendProducerDTO> getPageChunk(Page<Producer> producerPage) {
        PageChunk<RecommendProducerDTO> pageChunk = new PageChunk<>();
        pageChunk.setContent(getRecommendProducerDTO(producerPage.getContent()));
        pageChunk.setTotalPages(producerPage.getTotalPages());
        pageChunk.setTotalElements(producerPage.getTotalElements());
        pageChunk.setPageNumber(producerPage.getPageable().getPageNumber() + 1);
        pageChunk.setNumberOfElements(producerPage.getNumberOfElements());
        return pageChunk;
    }

    private List<RecommendProducerDTO> getRecommendProducerDTO(List<Producer> producerList) {
        List<RecommendProducerDTO> recommendProducerDTOList = new ArrayList<>();
        for (Producer producer :
                producerList) {
            RecommendProducerDTO recommendProducerDTO = new RecommendProducerDTO();
            recommendProducerDTO.setId(producer.getId());
            recommendProducerDTO.setName(producer.getName());

            recommendProducerDTOList.add(recommendProducerDTO);
        }
        return recommendProducerDTOList;
    }







}
