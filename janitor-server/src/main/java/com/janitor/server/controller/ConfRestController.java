package com.janitor.server.controller;

import com.janitor.common.model.*;
import com.janitor.server.service.JanitorConfigService;
import com.janitor.server.service.JanitorEventService;
import com.janitor.server.service.RegistryCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName ConfRestController
 * Description
 *
 * @author 曦逆
 * Date 2022/5/17 10:14
 */
@Controller
@RequestMapping({"/api"})
public class ConfRestController {
    @Autowired
    private JanitorConfigService janitorConfigService;
    @Autowired
    private JanitorEventService janitorEventService;
    @Autowired
    private RegistryCacheService registryCacheService;

    public ConfRestController() {
    }

    private Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("errorCode", 0);
        result.put("data", data);
        return result;
    }

    @ResponseBody
    @RequestMapping({"/registry"})
    public Map<String, Object> registry(@RequestBody RegistryBean registryBean) {
        this.janitorConfigService.registerConfig(registryBean);
        this.janitorEventService.registerEvent(registryBean);
        this.registryCacheService.addRegistryBean(registryBean.getApp(), registryBean);
        this.registryCacheService.flushToLocalCache();
        return this.success("");
    }

    @ResponseBody
    @RequestMapping({"/notify"})
    public Map<String, Object> notify(@RequestBody EventResultNotifyReq req) {
        boolean notify = this.janitorEventService.notify(req);
        return this.success(notify ? "消息确认录入成功" : "消息确认录入失败");
    }

    @ResponseBody
    @RequestMapping({"/event"})
    public EventPushResult eventSend(@RequestBody EventPushReq req) {
        return this.janitorEventService.pushEvent(req);
    }

    @ResponseBody
    @PostMapping({"/heartbeat"})
    public Map<String, Object> heartbeat(@RequestBody @Valid HeartbeatDTO heartbeatDTO) {
        boolean success = this.janitorEventService.heartBeat(heartbeatDTO);
        return this.success(success ? "心跳录入成功" : "心跳录入失败");
    }
}
