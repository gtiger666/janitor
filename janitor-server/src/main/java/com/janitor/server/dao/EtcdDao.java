package com.janitor.server.dao;

import com.janitor.common.etcd.EtcdServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * ClassName EtcdDao
 * Description
 *
 * @author 曦逆
 * Date 2022/5/17 8:50
 */
@Component
public class EtcdDao {

    private EtcdServiceV3 etcdServiceV3;
    @Autowired
    private Environment env;
    private static final String ETCD_SERVER_KEY = "janitor.etcd.server";
    private static final String ETCD_USER_KEY = "janitor.etcd.user";
    private static final String ETCD_AUTH_KEY = "janitor.etcd.authority";
    private static final String ETCD_PASS_KEY = "janitor.etcd.password";

    public EtcdDao() {
    }

    @PostConstruct
    public void init() {
        String[] urls = Objects.requireNonNull(this.env.getProperty(ETCD_SERVER_KEY)).split(",");
        if (this.env.getProperty(ETCD_AUTH_KEY, Boolean.class, false)) {
            this.etcdServiceV3 = new EtcdServiceV3(this.env.getProperty(ETCD_USER_KEY), this.env.getProperty(ETCD_PASS_KEY), urls);
        } else {
            this.etcdServiceV3 = new EtcdServiceV3(urls);
        }

    }

    public EtcdServiceV3 getEtcdServiceV3() {
        return this.etcdServiceV3;
    }

}
