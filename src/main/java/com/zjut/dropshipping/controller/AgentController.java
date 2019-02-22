package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.service.AgentService;
import com.zjut.dropshipping.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zjxjwxk
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

    private final AgentService agentService;
    private final FileService fileService;

    @Autowired
    public AgentController(AgentService agentService, FileService fileService) {
        this.agentService = agentService;
        this.fileService = fileService;
    }




    @PostMapping("/register")
    @ResponseBody
    public ServerResponse register(Agent agent, HttpSession session) {
        ServerResponse response = agentService.register(agent);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_AGENT, agent);
        }

        return response;
    }

    @PostMapping("/login")
    @ResponseBody
    public ServerResponse<Agent> login(String phone, String password, HttpSession session) {
        ServerResponse<Agent> response = agentService.login(phone, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_AGENT, response.getData());
        }

        return response;
    }

    @PostMapping("/IDCard-upload")
    @ResponseBody
    public ServerResponse upload(HttpSession session, HttpServletRequest request,
                                 @RequestParam(value = "upload_file") MultipartFile file,
                                 @RequestParam(value = "type")String type) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return fileService.IDCardUpload(file, path, type, agent.getId(), agent.getIdentityNumber());
    }

    @PostMapping("/request_agreement")
    @ResponseBody
    public ServerResponse requestAgreement(HttpSession session, Integer producerId) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        if (agent.getState().equals(Const.AccountState.UNREVIEWED)) {
            return ServerResponse.createByErrorMessage("还未进行实名认证或审核还未通过");
        } else if (agent.getState().equals(Const.AccountState.FROZEN)) {
            return ServerResponse.createByErrorMessage("账号已冻结");
        } else {
            return agentService.requestAgreement(producerId, agent.getId());
        }
    }

    @GetMapping("/get_producer_agreement_request")
    @ResponseBody
    public ServerResponse getProducerAgreementRequest(HttpSession session) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return agentService.getProducerAgreementRequest(agent.getId());
    }

    @PostMapping("/response_producer_agreement_request")
    @ResponseBody
    public ServerResponse responseProducerAgreementRequest(HttpSession session, Integer producerId, String response) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return agentService.responseProducerAgreementRequest(agent.getId(), producerId, response);
    }

    @GetMapping("/get_recommend_producer")
    @ResponseBody
    public ServerResponse getRecommendProducer(HttpSession session,
                                               @RequestParam(defaultValue = "1") Integer pageNumber,
                                               @RequestParam(defaultValue = "10") Integer numberOfElements) {

        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return agentService.getRecommendProducer(agent.getId(), pageNumber, numberOfElements);
    }


}
