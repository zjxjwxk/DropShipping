package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author ljx
 */
@RestController
@RequestMapping("/producer")
public class ProducerController {

    private final ProducerService producerService;

    @Autowired
    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ServerResponse register(Producer producer, HttpSession session) {
        ServerResponse response = producerService.register(producer);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_PRODUCER, producer);
        }
        return response;
    }

    @PostMapping("/login")
    @ResponseBody
    public ServerResponse<Producer> login(String contactPhone, String password, HttpSession session) {
        ServerResponse<Producer> response = producerService.login(contactPhone, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_PRODUCER, response.getData());
        }
        return response;
    }



    @GetMapping("/get_recommend_agent")
    @ResponseBody
    public ServerResponse getRecommendAgent(HttpSession session,
                                             @RequestParam(defaultValue = "1") Integer pageNumber,
                                             @RequestParam(defaultValue = "10") Integer numberOfElements) {

        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return producerService.getRecommendAgent( pageNumber, numberOfElements);
    }

    @GetMapping("/get_accepted_agent")
    @ResponseBody
    public ServerResponse getAcceptedAgent(HttpSession session,
                                            @RequestParam(defaultValue = "1") Integer pageNumber,
                                            @RequestParam(defaultValue = "10") Integer numberOfElements) {

        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return producerService.getAcceptedAgent(producer.getId(), pageNumber, numberOfElements);
    }

    @PostMapping("/producer_request_agreement")
    @ResponseBody
    public ServerResponse producerRequestAgreement(HttpSession session, Integer agentId) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if (producer.getState().equals(Const.AccountState.UNREVIEWED)) {
            return ServerResponse.createByErrorMessage("还未进行实名认证或审核还未通过");
        } else if (producer.getState().equals(Const.AccountState.FROZEN)) {
            return ServerResponse.createByErrorMessage("账号已冻结");
        } else {
            return producerService.producerRequestAgreement(agentId, producer.getId());
        }
    }

    @GetMapping("/get_agent_agreement_request")
    @ResponseBody
    public ServerResponse getProducerAgreementRequest(HttpSession session) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return producerService.getAgentAgreementRequest(producer.getId());
    }

    @PostMapping("/response_agent_agreement_request")
    @ResponseBody
    public ServerResponse responseAgentAgreementRequest(HttpSession session, Integer agentId, String response) {
        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return producerService.responseAgentAgreementRequest(producer.getId(), agentId, response);
    }

    @GetMapping("/get_detail_agent")
    @ResponseBody
    public ServerResponse getDetailAgent(HttpSession session,Integer agentId) {

        Producer producer = (Producer) session.getAttribute(Const.CURRENT_PRODUCER);
        if (producer == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return producerService.getDetailAgent(agentId);
    }
}
