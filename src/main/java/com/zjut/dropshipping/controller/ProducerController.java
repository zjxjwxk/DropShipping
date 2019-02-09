package com.zjut.dropshipping.controller;

import com.zjut.dropshipping.common.Const;
import com.zjut.dropshipping.common.ServerResponse;
import com.zjut.dropshipping.dataobject.Producer;
import com.zjut.dropshipping.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
            session.setAttribute(Const.CURRENT_USER, producer);
        }
        return response;
    }

    @PostMapping("/login")
    @ResponseBody
    public ServerResponse<Producer> login(String contactPhone, String password, HttpSession session) {
        ServerResponse<Producer> response = producerService.login(contactPhone, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }
}
