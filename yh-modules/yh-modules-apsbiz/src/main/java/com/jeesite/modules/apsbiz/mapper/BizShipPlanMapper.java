package com.jeesite.modules.apsbiz.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jeesite.modules.apsbiz.entity.BizShipPlan;
import com.jeesite.common.base.YhBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 靠离泊计划
 *
 * @author zhudi
 * @date 2024-12-08 21:19:19
 */
@Mapper
public interface BizShipPlanMapper extends YhBaseMapper<BizShipPlan> {



    @Select("SELECT " +
            "   A.*, " +
            "   B.VOYAGE_NO AS voyageNo, " +
            "	B.SHIP_CODE AS shipCode, " +
            "	B.SHIP_NAME_CN AS shipNameCn, " +
            "	B.WORKING_COMPANY AS workingCompany, " +
            "	B.AGENT_COMPANY AS agentCompany, " +
            "	B.DRAFT_IN AS draftIn, " +
            "	B.DRAFT_OUT AS draftOut, " +
            "	B.CARGO_SUB_TYPE_CODE AS cargoSubTypeCode, " +
            "	B.CARGO_TYPE_NAME AS cargoTypeName, " +
            "	B.TRADE_TYPE AS tradeType, " +
            "	B.LOAD_UNLOAD AS loadUnload, " +
            "	B.LOAD_PLAN_WEIGHT AS loadPlanWeight, " +
            "	B.UNLOAD_PLAN_WEIGHT AS unloadPlanWeight, " +
            "	B.CARRY_WEIGHT AS carryWeight, " +
            "	B.EXPECT_ARRIVE_TIME AS expectArriveTime, " +
            "	B.EXPECT_LEAVE_TIME AS expectLeaveTime, " +
            "	B.PORT_CODE_FROM AS portCodeFrom, " +
            "	B.PORT_CODE_TO AS portCodeTo, " +
//            "	B.NEED_LEAD AS needLead, " +
            "	B.IS_DELOAD AS isDeload, " +
//            "	B.NEED_TRACTOR AS needTractor, " +
            "	B.CAR_RAEDY_TIME AS carRaedyTime, " +
            "	B.IS_IMPORTANT AS isImportant, " +
            "	B.IS_SPECIAL AS isSpecial, " +
            "	B.CARGO_OWNER AS cargoOwner, " +
            "	B.REMARK AS remark, " +
            "	B.CREATE_COMPANY AS createCompany, " +
            "	B.STATUS AS status, " +
            "	B.ALGORITHM_STATE AS algorithmState, " +
            "	C.SHIP_LENGTH AS shipLength, " +
            "	C.SHIP_WIDTH AS shipWidth " +
            "FROM ALG_SHIP_PLAN A LEFT JOIN ALG_SHIP_FORECAST B ON A.SHIP_FORCAST_ID = B.ID LEFT JOIN ALG_SHIP_INFO C ON B.SHIP_CODE = C.SHIP_CODE " +
            "${ew.customSqlSegment}")
    List<BizShipPlan> queryList(@Param(Constants.WRAPPER) QueryWrapper<BizShipPlan> queryWrapper);



    @Select("SELECT " +
            " COUNT(A.ID) " +
            "FROM ALG_SHIP_PLAN A LEFT JOIN ALG_SHIP_FORECAST B ON A.SHIP_FORCAST_ID = B.ID " +
            "${ew.customSqlSegment}"
            )
    Long pageCount(@Param(Constants.WRAPPER) QueryWrapper<BizShipPlan> queryWrapper);


    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tTMP.*,\n" +
            "\t\tROWNUM ROW_ID \n" +
            "\tFROM\n" +
            "\t\t( " +
            "SELECT " +
            "   A.*, " +
            "   B.VOYAGE_NO AS voyageNo, " +
            "	B.SHIP_CODE AS shipCode, " +
            "	B.SHIP_NAME_CN AS shipNameCn, " +
            "	B.WORKING_COMPANY AS workingCompany, " +
            "	B.AGENT_COMPANY AS agentCompany, " +
            "	B.DRAFT_IN AS draftIn, " +
            "	B.DRAFT_OUT AS draftOut, " +
            "	B.CARGO_SUB_TYPE_CODE AS cargoSubTypeCode, " +
            "	B.CARGO_TYPE_NAME AS cargoTypeName, " +
            "	B.TRADE_TYPE AS tradeType, " +
            "	B.LOAD_UNLOAD AS loadUnload, " +
            "	B.LOAD_PLAN_WEIGHT AS loadPlanWeight, " +
            "	B.UNLOAD_PLAN_WEIGHT AS unloadPlanWeight, " +
            "	B.CARRY_WEIGHT AS carryWeight, " +
            "	B.EXPECT_ARRIVE_TIME AS expectArriveTime, " +
            "	B.EXPECT_LEAVE_TIME AS expectLeaveTime, " +
            "	B.PORT_CODE_FROM AS portCodeFrom, " +
            "	B.PORT_CODE_TO AS portCodeTo, " +
//            "	B.NEED_LEAD AS needLead, " +
            "	B.IS_DELOAD AS isDeload, " +
//            "	B.NEED_TRACTOR AS needTractor, " +
            "	B.CAR_RAEDY_TIME AS carRaedyTime, " +
            "	B.IS_IMPORTANT AS isImportant, " +
            "	B.IS_SPECIAL AS isSpecial, " +
            "	B.CARGO_OWNER AS cargoOwner, " +
            "	B.REMARK AS remark, " +
            "	B.CREATE_COMPANY AS createCompany, " +
            "	B.STATUS AS status, " +
            "	B.ALGORITHM_STATE AS algorithmState, " +
            "	C.SHIP_LENGTH AS shipLength, " +
            "	C.SHIP_WIDTH AS shipWidth " +
            "FROM ALG_SHIP_PLAN A LEFT JOIN ALG_SHIP_FORECAST B ON A.SHIP_FORCAST_ID = B.ID LEFT JOIN ALG_SHIP_INFO C ON B.SHIP_CODE = C.SHIP_CODE " +
            "${ew.customSqlSegment}" +
            " ) TMP \n" +
            "\tWHERE\n" +
            "\t\tROWNUM <= #{size} \n" +
            "\t) \n" +
            "WHERE\n" +
            "\tROW_ID > #{page}")
    List<BizShipPlan> page(@Param(Constants.WRAPPER) QueryWrapper<BizShipPlan> queryWrapper, @Param("page") Integer page, @Param("size") Integer size);

}
