package com.cumt.provider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cumt.provider.domain.CoalFlow;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ICoalFlowService extends IService<CoalFlow> {
    @Transactional
    CompletableFuture<Void> CoalFlowToMysql(List<String> data);
    CoalFlow getLatestFlow();
}
