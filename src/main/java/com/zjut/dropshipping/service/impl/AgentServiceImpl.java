package com.zjut.dropshipping.service.impl;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.repository.AgentRepository;
import com.zjut.dropshipping.service.AgentService;
import com.zjut.dropshipping.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjxjwxk
 */
@Service("AgentService")
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
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

        validResponse = this.checkValid(agent.getExternalShop(), Const.EXTERNAL_SHOP);
        if (validResponse.isError()) {
            return validResponse;
        }

        agent.setState(Const.State.NORMAL);

        // MD5加密
        agent.setPassword(MD5Util.MD5EncodeUtf8(agent.getPassword()));

        agentRepository.save(agent);

        return ServerResponse.createBySuccessMessage("注册成功");
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
