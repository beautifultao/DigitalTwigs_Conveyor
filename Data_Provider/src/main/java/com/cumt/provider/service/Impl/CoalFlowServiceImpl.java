package com.cumt.provider.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.provider.domain.CoalFlow;
import com.cumt.provider.mapper.CoalFlowMapper;
import com.cumt.provider.service.ICoalFlowService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CoalFlowServiceImpl extends ServiceImpl<CoalFlowMapper, CoalFlow> implements ICoalFlowService {
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    @Async("taskExecutor")  //使用自定义的taskExecutor线程池
    public CompletableFuture<Void> CoalFlowToMysql(List<String> data) {
        List<CoalFlow> coalFlows = new ArrayList<>();

        if(data != null && data.size() > 0) {
            for (String json : data) {
                CoalFlow coalFlow = JSON.parseObject(json, CoalFlow.class);
                coalFlow.setId(null);
                coalFlows.add(coalFlow);
            }
        }
        this.saveBatch(coalFlows);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CoalFlow getLatestFlow() {
        if(Boolean.TRUE.equals(redisTemplate.hasKey("Conveyor"))){
            String jsonData = redisTemplate.opsForList().index("Conveyor", 0);
            return JSON.parseObject(jsonData,CoalFlow.class);
        } else {
            return null;
        }
    }


    /**
     *  (cron = "秒 分 时 日 月 星期 年")
     *  每天0点，12点，18点自动执行CoalFlowToMysql()
     */
    @Scheduled(cron = "0 0 0,9,12,15,18 * * ?")
    public void scheduledCheckAndProcess() {
        List<String> data = redisTemplate.opsForList().range("Conveyor", 0, -1);

        if (data != null && data.size() > 3000) {
            int pageSize = 1000; // 设置每页的大小
            // 存储所有异步任务的 Future
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            // 将数据按页进行分割
            for (int i = 0; i < data.size(); i += pageSize) {
                int end = Math.min(i + pageSize, data.size()); // 确定每页数据的结束索引
                List<String> page = data.subList(i, end); // 获取当前页的数据子列表
                CompletableFuture<Void> future = CoalFlowToMysql(page); // 异步上传当前页数据到 MySQL
                futures.add(future); // 将 Future 添加到列表中
            }

            // 等待所有异步任务完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            redisTemplate.delete("Conveyor"); // 删除 Redis 中的 "Conveyor" 列表
        }
    }
}
