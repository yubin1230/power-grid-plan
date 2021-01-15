package com.power.grid.plan.service.thread;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.service.manager.AntCalculateManage;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 多线程执行
 * @author yubin
 * @date 2021/1/10 13:57
 */
public class AntCalculateTask implements Callable<List<HandleBo>> {

    private AntCalculateManage antCalculateManage;


    public AntCalculateTask(@NonNull AntCalculateManage antCalculateManage) {
        this.antCalculateManage = antCalculateManage;
    }

    @Override
    public List<HandleBo> call() {
        return antCalculateManage.handle();
    }
}
