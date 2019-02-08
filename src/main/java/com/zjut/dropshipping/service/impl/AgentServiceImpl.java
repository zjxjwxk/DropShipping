package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.dataobject.Agreement;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.dto.ProducerAgreementRequestDTO;
import com.zjut.dropshipping.repository.AgentRepository;
import com.zjut.dropshipping.repository.AgreementRepository;
import com.zjut.dropshipping.repository.ProducerRepository;
import com.zjut.dropshipping.service.AgentService;
import com.zjut.dropshipping.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjxjwxk
 */
@Service("AgentService")
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final ProducerRepository producerRepository;
    private final AgreementRepository agreementRepository;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository,
                            ProducerRepository producerRepository,
                            AgreementRepository agreementRepository) {
        this.agentRepository = agentRepository;
        this.producerRepository = producerRepository;
        this.agreementRepository = agreementRepository;
    }

    @Override
    public ServerResponse<String> register(Agent agent) {

        // 校验电话号码
        ServerResponse<String> validResponse = this.checkValid(agent.getPhone(), Const.PHONE);
        if (validResponse.isError()) {
            return validResponse;
        }

        // 校验身份证号
        validResponse = this.checkValid(agent.getIdentityNumber(), Const.IDENTITY_NUMBER);
        if (validResponse.isError()) {
            return validResponse;
        }

        // 检验店铺名
        validResponse = this.checkValid(agent.getExternalShop(), Const.EXTERNAL_SHOP);
        if (validResponse.isError()) {
            return validResponse;
        }

        // 设置状态为未审核
        agent.setState(Const.AgentState.UNREVIEWED);

        // MD5加密
        agent.setPassword(MD5Util.MD5EncodeUtf8(agent.getPassword()));

        agentRepository.save(agent);

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<Agent> login(String phone, String password) {
        int resultCount = agentRepository.countByPhone(phone);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("该手机号码未被注册");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        Agent agent = agentRepository.findByPhoneAndPassword(phone, md5Password);
        if (agent == null) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }

        agent.setPassword(null);
        return ServerResponse.createBySuccess("登陆成功", agent);
    }

    @Override
    public ServerResponse<String> requestAgreement(Integer producerId, Integer agentId) {
        Agreement agreement = agreementRepository.findByProducerIdAndAgentId(producerId, agentId);
        if (agreement == null) {
            Agreement agreement1 = new Agreement();
            agreement1.setProducerId(producerId);
            agreement1.setAgentId(agentId);
            agreement1.setState("代理发送请求");
            agreementRepository.save(agreement1);
            return ServerResponse.createBySuccess("请求发送成功");
        } else if (agreement.getState().equals(Const.AgreementState.NORMAL)) {
            return ServerResponse.createByErrorMessage("已达成协议");
        } else if (agreement.getState().equals(Const.AgreementState.AGENT_REQUEST)) {
            return ServerResponse.createByErrorMessage("请求已发送");
        } else {
            return ServerResponse.createBySuccess("达成协议");
        }
    }

    @Override
    public ServerResponse getProducerAgreementRequest(Integer agentId) {
        List<Agreement> agreementList = agreementRepository.findByAgentIdAndState(agentId, Const.AgreementState.PRODUCER_REQUEST);
        if (agreementList.size() == 0) {
            return ServerResponse.createByErrorMessage("还没有厂商请求协议");
        }
        return ServerResponse.createBySuccess(this.getProducerAgreementRequestList(agreementList));
    }

    private List<ProducerAgreementRequestDTO> getProducerAgreementRequestList(List<Agreement> agreementList) {
        List<ProducerAgreementRequestDTO> producerAgreementRequestDTOList = new ArrayList<>();
        for (Agreement agreement :
                agreementList) {
            Producer producer = producerRepository.findOneById(agreement.getProducerId());
            ProducerAgreementRequestDTO producerAgreementRequestDTO = new ProducerAgreementRequestDTO();
            producerAgreementRequestDTO.setId(producer.getId());
            producerAgreementRequestDTO.setName(producer.getName());
            producerAgreementRequestDTO.setContactName(producer.getName());
            producerAgreementRequestDTO.setContactPhone(producer.getContactPhone());
            producerAgreementRequestDTO.setRegion(producer.getRegion());
            producerAgreementRequestDTO.setRegisterCapital(producer.getRegisterCapital());
            producerAgreementRequestDTO.setRegisterTime(producer.getRegisterTime());
            producerAgreementRequestDTO.setContent(producer.getContent());
            producerAgreementRequestDTO.setRequestTime(agreement.getTime());

            producerAgreementRequestDTOList.add(producerAgreementRequestDTO);
        }
        return producerAgreementRequestDTOList;
    }

    private ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 开始校验
            if (Const.PHONE.equals(type)) {
                int resultCount = agentRepository.countByPhone(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该手机已被注册");
                }
            }
            if (Const.IDENTITY_NUMBER.equals(type)) {
                int resultCount = agentRepository.countByIdentityNumber(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该身份证已被注册");
                }
            }
            if (Const.EXTERNAL_SHOP.endsWith(type)) {
                int resultCount = agentRepository.countByExternalShop(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("该店铺名已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }
}
