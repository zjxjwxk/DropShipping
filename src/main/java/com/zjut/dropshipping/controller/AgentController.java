package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ResponseCode;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Agent;
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

    /**
     * 代理商注册
     * @param agent 代理商对象信息
     * @param session session
     * @return 服务响应对象
     */
    @PostMapping("/register")
    @ResponseBody
    public ServerResponse register(Agent agent, HttpSession session) {
        ServerResponse response = agentService.register(agent);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_AGENT, agent);
        }
        return response;
    }

    /**
     * 代理商登录
     * @param phone 手机号
     * @param password 密码
     * @param session session
     * @return 服务响应对象
     */
    @PostMapping("/login")
    @ResponseBody
    public ServerResponse<Agent> login(String phone, String password, HttpSession session) {
        ServerResponse<Agent> response = agentService.login(phone, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_AGENT, response.getData());
        }
        return response;
    }

    /**
     * 代理商上传身份证照片
     * @param session session
     * @param request servlet请求对象
     * @param file 文件
     * @param type 文件类型 (IDCard-1 或 IDCard-2) (即正面和反面)
     * @return 服务响应对象
     */
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

    /**
     * 代理商请求厂商合作协议
     * @param session session
     * @param producerId 厂商id
     * @return 服务响应对象
     */
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

    /**
     * 代理商获取（合作中/请求合作）厂商列表
     * @param session session
     * @param state 合作厂商状态（"正常" 或 "厂商发送请求"）
     * @return 服务响应对象
     */
    @GetMapping("/get_agreement_producer")
    @ResponseBody
    public ServerResponse getAgreementProducer(HttpSession session, String state) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return agentService.getAgreementProducer(agent.getId(), state);
    }

    /**
     * 代理商回应厂商合作请求
     * @param session session
     * @param producerId 厂商id
     * @param response 回应方式 (accept 或 refuse)
     * @return 服务响应对象
     */
    @PostMapping("/response_producer_agreement_request")
    @ResponseBody
    public ServerResponse responseProducerAgreementRequest(HttpSession session, Integer producerId, String response) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return agentService.responseProducerAgreementRequest(agent.getId(), producerId, response);
    }

    /**
     * 代理商解除与厂商的合作协议
     * @param session session
     * @param producerId 厂商id
     * @return 服务响应对象
     */
    @PostMapping("/cancel_agreement")
    @ResponseBody
    public ServerResponse cancelAgreement(HttpSession session, Integer producerId) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return agentService.cancelAgreement(agent.getId(), producerId);
    }

    /**
     * 代理商获取推荐厂商列表
     * @param session session
     * @param pageNumber 当前页码数
     * @param numberOfElements 每页的数量
     * @return 服务响应对象
     */
    @GetMapping("/get_recommend_producer")
    @ResponseBody
    public ServerResponse getRecommendProducer(HttpSession session,
                                               @RequestParam(defaultValue = "1") Integer pageNumber,
                                               @RequestParam(defaultValue = "10") Integer numberOfElements) {

        Agent agent = (Agent) session.getAttribute(Const.CURRENT_AGENT);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return agentService.getRecommendProducer( pageNumber, numberOfElements);
    }
}
