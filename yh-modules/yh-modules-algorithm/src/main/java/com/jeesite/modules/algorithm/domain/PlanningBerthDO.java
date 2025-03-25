package com.jeesite.modules.algorithm.domain;


import com.jeesite.modules.algorithm.entity.AlgBerthCargoEffi;
import com.jeesite.modules.algorithm.entity.AlgBerthInfo;
import com.jeesite.modules.algorithm.planning.PlanningContext;
import lombok.Data;

import java.util.List;


@Data
public class PlanningBerthDO {



    private final AlgBerthInfo berthInfo; // 泊位信息
    private final List<PlanningBollardDO> bollardDOList; // 榄柱信息
    private List<AlgBerthCargoEffi> berthCargoEffiList; // 作业效率
    private PlanningContext planningContext;


    private boolean bollardSortAsc;














    /**
     * 是第一个缆
     * @return
     */
    public boolean isFirstBollard(String bollardId){
        return this.bollardDOList.get(0).getBollardInfo().getId().equals(bollardId);
    }

    /**
     * 是最后一个缆
     * @return
     */
    public boolean isLastBollard(String bollardId){
        return this.bollardDOList.get(this.bollardDOList.size()-1).getBollardInfo().getId().equals(bollardId);
    }


    @Override
    public String toString() {
        return "PlanningBerthDTO{" +
                "berthInfo=" + berthInfo +
                '}';
    }
}
