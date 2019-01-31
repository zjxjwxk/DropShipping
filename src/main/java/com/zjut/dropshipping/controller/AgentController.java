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

    @PostMapping("/register")
    @ResponseBody
    public ServerResponse<String> register(Agent agent, HttpSession session) {
        ServerResponse<String> response = agentService.register(agent);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    @PostMapping("/login")
    @ResponseBody
    public ServerResponse<Agent> login(String phone, String password, HttpSession session) {
        ServerResponse<Agent> response = agentService.login(phone, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    @PostMapping("/IDCard-upload")
    @ResponseBody
    public ServerResponse upload(HttpSession session, HttpServletRequest request,
                                 @RequestParam(value = "upload_file") MultipartFile file,
                                 @RequestParam(value = "type")String type) {
        Agent agent = (Agent) session.getAttribute(Const.CURRENT_USER);
        if (agent == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        // 填充业务
        String path = request.getSession().getServletContext().getRealPath("upload");
        return fileService.IDCardUpload(file, path, type, agent.getId(), agent.getIdentityNumber());
    }
}
