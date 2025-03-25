ALTER TABLE "APSYL"."ALG_SHORE_MACHINE" RENAME COLUMN "MACHINE_CODE" TO "MACHINE_TYPE_CODE";
ALTER TABLE "APSYL"."ALG_SHORE_MACHINE" MODIFY ("MACHINE_TYPE_CODE" NUMBER(2,0));
ALTER TABLE "APSYL"."ALG_SHORE_MACHINE" DROP ("BERTH_NO");


CREATE TABLE "APSYL"."ALG_BERTH_MACHINE_REL"
(
    "MACHINE_NO" VARCHAR2(32),
    "BERTH_NO"   VARCHAR2(32),
    "ORDER_IDX" NUMBER(3)
)
;

COMMENT
ON COLUMN "APSYL"."ALG_BERTH_MACHINE_REL"."MACHINE_NO" IS '机械编号';

COMMENT
ON COLUMN "APSYL"."ALG_BERTH_MACHINE_REL"."BERTH_NO" IS '泊位编号';
COMMENT
ON COLUMN "APSYL"."ALG_BERTH_MACHINE_REL"."ORDER_IDX" IS '排序序号';




--------------------------
-- 1. 船舶作业计划主表
--------------------------
CREATE TABLE ALG_SHIP_WORK_PLAN
(
    ID                VARCHAR2(50) PRIMARY KEY, -- 主表唯一ID
    VOYAGE_NO         VARCHAR2(50),             -- 航次号申报唯一ID
    BERTH_NO          VARCHAR2(20),             -- 泊位号
    BERTH_DIRECTION   VARCHAR2(100),            -- 靠泊要求（着岸）
    HEAD_BOLLARD_ID   VARCHAR2(20),             -- 首榄桩编号
    TAIL_BOLLARD_ID   VARCHAR2(20),             -- 尾榄桩编号
    SHIP_NAME         VARCHAR2(100),            -- 船舶名称
    CABIN_NUM         NUMBER(4),                -- 货舱数量
    CARRY_WEIGHT      NUMBER(12,2),             -- 载货总吨数
    SHIP_LENGTH       NUMBER(8,2),              -- 船舶长度（米）
    DRAFT             NUMBER(6,2),              -- 船舶吃水深度（米）
    CARGO_NAME        VARCHAR2(100),            -- 货物名称
    CARGO_OWNER       VARCHAR2(100),            -- 货物所有者
    AGENT             VARCHAR2(100),            -- 代理公司
    WORK_CONTENT             VARCHAR2(255),            --作业内容
    PLAN_TIME   TIMESTAMP,                -- 计划时间
    ALGORITHM_STATE  NUMBER(4),                -- 计划时间
    PLAN_BERTH_TIME   TIMESTAMP,                -- 计划靠泊时间
    BERTH_TIME        TIMESTAMP,                -- 实际靠泊时间
    PLAN_FINISH_TIME  TIMESTAMP,                -- 计划完成时间
    THROUGHPUT_WEIGHT NUMBER(12,2),             -- 吞吐总吨数
    REMAINING_WEIGHT  NUMBER(12,2)              -- 剩余吨数
);
COMMENT
ON TABLE ALG_SHIP_WORK_PLAN IS '船舶作业计划主表，记录船舶作业核心信息';

--------------------------
-- 2. 作业计划班次表
--------------------------
CREATE TABLE ALG_SHIP_WORK_SHIFT
(
    ID                VARCHAR2(50) PRIMARY KEY, -- 班次唯一ID
    SHIP_WORK_PLAN_ID VARCHAR2(50),             -- 关联主表ID
    SHIFT_TYPE        NUMBER(1),                -- 班次类型（1=夜班，2=昼班）
    WORKLOAD          NUMBER(10,2),             -- 班次作业量（吨）
    START_TIME        TIMESTAMP,                -- 班次开始时间
    END_TIME          TIMESTAMP,                -- 班次结束时间
    WORK_HOURS        NUMBER(6,2),              -- 作业时长（小时）
    MECH_EFFICIENCY   NUMBER(5,2),              -- 总机械效率（百分比）
    MANPOWER_ARRANGE  VARCHAR2(200)             -- 人力安排描述

);
COMMENT
ON TABLE ALG_SHIP_WORK_SHIFT IS '作业班次明细表，记录每个班次的具体作业计划';

--------------------------
-- 3. 作业计划班次机械分配表
--------------------------
CREATE TABLE ALG_SHIP_MACHINE_ALLOC
(
    ID                VARCHAR2(50) PRIMARY KEY,
    VOYAGE_NO VARCHAR2(50),  -- 船舶唯一航次号
    MACHINE_ID      VARCHAR2(50),  -- 机械ID
    MACHINE_NAME      VARCHAR2(100), -- 机械名称
    MACHINE_CLASS_CODE NUMBER(2),  -- 机械类型（岸基/流动机械）
    MACHINE_COUNT     NUMBER(3)      -- 机械数量
);
COMMENT
ON TABLE ALG_SHIP_MACHINE_ALLOC IS '船舶机械分配表，记录每条船使用的机械设备';

--------------------------
-- 4. 筒仓基础数据表
--------------------------
CREATE TABLE ALG_SILO_BASE
(
    ID                 VARCHAR2(50) PRIMARY KEY, -- 筒仓唯一ID
    SILO_NO            VARCHAR2(20),             -- 筒仓编号
    BELONG_PERIOD      VARCHAR2(50),             -- 所属期
    TOTAL_CAPACITY     NUMBER(12,2),             -- 总容量（吨）
    REAL_TIME_CAPACITY NUMBER(12,2),             -- 实时容量
    OCCUPIED_CAPACITY  NUMBER(12,2),             -- 占用容量
    AVAILABLE_CAPACITY NUMBER(12,2)              -- 可用容量
);
COMMENT
ON TABLE ALG_SILO_BASE IS '筒仓基础数据表，记录筒仓静态属性';

--------------------------
-- 5. 筒仓实时数据表
--------------------------
CREATE TABLE ALG_SILO_REALTIME
(
    ID              VARCHAR2(50) PRIMARY KEY, -- 记录唯一ID
    SILO_NO         VARCHAR2(20),             -- 筒仓编号
    STAT_DATE       TIMESTAMP,                -- 统计日期
    CARGO_NAME      VARCHAR2(100),            -- 当前货物名称
    CARGO_ORIGIN    VARCHAR2(100),            -- 货物产地
    CARGO_OWNER     VARCHAR2(100),            -- 货主名称
    TOTAL_CAPACITY  NUMBER(12,2),             -- 总容量（吨）
    HANDOVER_AMOUNT NUMBER(10,2),             -- 交班数量
    TAKEOVER_AMOUNT NUMBER(10,2),             -- 接班数量
    INBOUND_QTY     NUMBER(10,2),             -- 进仓量
    OUTBOUND_QTY    NUMBER(10,2),             -- 出仓量
    CLEARANCE_DIFF  NUMBER(10,2)              -- 清空货差
);
COMMENT
ON TABLE ALG_SILO_REALTIME IS '筒仓实时数据表，记录动态库存变化';

--------------------------
-- 6. 筒仓泊位关系表
--------------------------
CREATE TABLE ALG_SILO_BERTH_REL
(
    SILO_NO  VARCHAR2(20), -- 筒仓编号
    BERTH_NO VARCHAR2(20)  -- 泊位编号

);
COMMENT
ON TABLE ALG_SILO_BERTH_REL IS '筒仓与泊位关联关系表';

--------------------------
-- 7. 船舶作业筒仓安排表
--------------------------
CREATE TABLE ALG_SHIP_SILO_ARRANGE
(
    VOYAGE_NO VARCHAR2(50), -- 航次号申报唯一ID
    SILO_NO   VARCHAR2(20), -- 筒仓编号
);
COMMENT
ON TABLE ALG_SHIP_SILO_ARRANGE IS '船舶作业筒仓分配表，记录船舶作业占用的筒仓';

--------------------------
-- 8. 皮带流程基础数据表
--------------------------
CREATE TABLE ALG_BELT_PROCESS
(
    ID              VARCHAR2(50) PRIMARY KEY, -- 流程唯一ID
    PROCESS_NO      VARCHAR2(20),             -- 流程编号
    PROCESS_NAME    VARCHAR2(100),            -- 流程名称
    FLOW_RATE       NUMBER(10,2),             -- 流程流量（吨/小时）
    PROCESS_ATTR    VARCHAR2(100),            -- 流程属性
    HEAD_DEVICE     VARCHAR2(50),             -- 头设备编号
    HEAD_LINK_SHORE VARCHAR2(50),             -- 头设备关联岸机设备
    HEAD_LINK_SILO  VARCHAR2(20),             -- 头设备关联筒仓号
    MID_DEVICE      VARCHAR2(2047),             -- 中间设备编号
    TAIL_DEVICE     VARCHAR2(50),             -- 尾设备编号
    TAIL_LINK_SILO  VARCHAR2(20)              -- 尾设备关联筒仓号
);
COMMENT
ON TABLE ALG_BELT_PROCESS IS '皮带流程基础数据表，记录皮带运输系统配置';

--------------------------
-- 9. 船舶作业皮带占用表
--------------------------
CREATE TABLE ALG_BELT_PROCESS_OCC
(
    VOYAGE_NO           VARCHAR2(50), -- 航次号申报唯一ID
    OCCUPIED_PROCESS_NO VARCHAR2(20)  -- 占用皮带流程编号
);
COMMENT
ON TABLE ALG_BELT_PROCESS_OCC IS '船舶作业皮带占用表，记录船舶占用的皮带流程';

--------------------------
-- 10. 堆场基础数据表
--------------------------
CREATE TABLE ALG_YARD
(
    ID                 VARCHAR2(50) PRIMARY KEY, -- 堆场唯一ID
    YARD_NAME          VARCHAR2(100),            -- 堆场名称
    YARD_NO            VARCHAR2(20),             -- 堆场编号
    BELONG_ZONE        VARCHAR2(50),             -- 所属区域
    TOTAL_CAPACITY     NUMBER(12,2),             -- 总容量（吨）
    REAL_TIME_CAPACITY NUMBER(12,2),             -- 实时容量
    OCCUPIED_CAPACITY  NUMBER(12,2),             -- 占用容量
    AVAILABLE_CAPACITY NUMBER(12,2)              -- 可用容量
);
COMMENT
ON TABLE ALG_YARD IS '堆场基础数据表，记录堆场静态属性';

--------------------------
-- 11. 堆场泊位关系表
--------------------------
CREATE TABLE ALG_YARD_BERTH_REL
(
    YARD_NO  VARCHAR2(20), -- 堆场编号
    BERTH_NO VARCHAR2(20)  -- 泊位编号
);
COMMENT
ON TABLE ALG_YARD_BERTH_REL IS '堆场与泊位关联关系表';

--------------------------
-- 12. 堆场货种关系表
--------------------------
CREATE TABLE ALG_YARD_CARGO_REL
(
    YARD_NO         VARCHAR2(20), -- 堆场编号
    CARGO_TYPE_NAME VARCHAR2(50)  -- 货种大类
);
COMMENT
ON TABLE ALG_YARD_CARGO_REL IS '堆场与货种关联关系表';

--------------------------
-- 13. 堆场实时数据表
--------------------------
CREATE TABLE ALG_YARD_REALTIME
(
    ID                     VARCHAR2(50) PRIMARY KEY, -- 记录唯一ID
    YARD_NO                VARCHAR2(20),             -- 堆场编号
    STAT_DATE              TIMESTAMP,                -- 统计日期
    CARGO_TYPE             VARCHAR2(100),            -- 货种大类
    CARGO_ORIGIN           VARCHAR2(100),            -- 货物产地
    CARGO_OWNER            VARCHAR2(100),            -- 作业委托人（货主）
    STORAGE_CAPACITY       NUMBER(12,2),             -- 堆存总容量
    BELONG_AREA            VARCHAR2(50),             -- 所属区域（几期）
    YESTERDAY_STOCK        NUMBER(12,2),             -- 昨日库存数
    INBOUND_TIME           TIMESTAMP,                -- 入库时间
    YESTERDAY_INBOUND      NUMBER(12,2),             -- 上昼夜入库数
    YESTERDAY_TRANSFER_IN  NUMBER(12,2),             -- 上昼夜倒运入库数
    INVENTORY              NUMBER(12,2),             --  盘点数
    YESTERDAY_OUTBOUND     NUMBER(12,2),             -- 上昼夜出库数
    YESTERDAY_TRANSFER_OUT NUMBER(12,2),             -- 上昼夜倒运出库数
    CURRENT_STOCK          NUMBER(12,2)              -- 当前库存数
);
COMMENT
ON TABLE ALG_YARD_REALTIME IS '堆场实时数据表，记录动态库存变化';

--------------------------
-- 14. 船舶作业堆场安排表
--------------------------
CREATE TABLE ALG_SHIP_YARD_ARRANGE
(
    VOYAGE_NO VARCHAR2(50),  -- 航次号申报唯一ID
    YARD_NAME VARCHAR2(100), -- 堆场名称
    YARD_NO   VARCHAR2(20)   -- 堆场编号
);
COMMENT
ON TABLE ALG_SHIP_YARD_ARRANGE IS '船舶作业堆场分配表，记录船舶与堆场的关联';

--------------------------
-- 1. ALG_SHIP_WORK_PLAN（船舶作业计划主表）
--------------------------
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.ID IS '主表唯一标识符';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.VOYAGE_NO IS '航次号（唯一标识申报航次）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.BERTH_NO IS '船舶停靠的泊位编号';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.BERTH_DIRECTION IS '靠泊方向描述（如：左舷靠泊、右舷靠泊）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.HEAD_BOLLARD_ID IS '船舶首部固定缆绳的缆桩编号';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.TAIL_BOLLARD_ID IS '船舶尾部固定缆绳的缆桩编号';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.SHIP_NAME IS '船舶注册名称';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.CABIN_NUM IS '船舶货舱总数';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.CARRY_WEIGHT IS '船舶载货总重量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.SHIP_LENGTH IS '船舶总长度（单位：米）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.DRAFT IS '船舶吃水深度（单位：米）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.CARGO_NAME IS '装载货物名称';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.CARGO_OWNER IS '货物所有权归属方';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.AGENT IS '船舶代理公司名称';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.PLAN_BERTH_TIME IS '计划靠泊时间（精确到秒）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.BERTH_TIME IS '实际靠泊完成时间';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.PLAN_FINISH_TIME IS '计划作业完成时间';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.THROUGHPUT_WEIGHT IS '已完成货物吞吐量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.REMAINING_WEIGHT IS '剩余待作业货物量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.WORK_CONTENT IS '作业内容';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.PLAN_TIME IS '作业时间';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN.ALGORITHM_STATE IS '算法状态';


--------------------------
-- 2. ALG_SHIP_WORK_SHIFT（作业班次表）
--------------------------
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.ID IS '班次唯一标识符';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.SHIP_WORK_PLAN_ID IS '关联的船舶作业计划ID';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.SHIFT_TYPE IS '班次类型（1=夜班，2=昼班）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.WORKLOAD IS '本班次计划作业量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.START_TIME IS '班次开始时间（精确到秒）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.END_TIME IS '班次结束时间（精确到秒）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.WORK_HOURS IS '实际作业时长（单位：小时）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.MECH_EFFICIENCY IS '机械综合效率（百分比，如80.5）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT.MANPOWER_ARRANGE IS '人力配置详情（如：装卸工10人）';

--------------------------
-- 3. ALG_SHIFT_MACHINE_ALLOC（船舶机械分配表）
--------------------------
COMMENT ON COLUMN ALG_SHIP_MACHINE_ALLOC.ID IS '分配记录唯一ID';
COMMENT ON COLUMN ALG_SHIP_MACHINE_ALLOC.VOYAGE_NO IS '船舶唯一航次号';
COMMENT ON COLUMN ALG_SHIP_MACHINE_ALLOC.MACHINE_ID IS '机械设备ID';
COMMENT ON COLUMN ALG_SHIP_MACHINE_ALLOC.MACHINE_NAME IS '机械设备名称（如：门座起重机）';
COMMENT ON COLUMN ALG_SHIP_MACHINE_ALLOC.MACHINE_CLASS_CODE IS '机械类型编码（岸基/流动机械）';
COMMENT ON COLUMN ALG_SHIP_MACHINE_ALLOC.MACHINE_COUNT IS '分配数量（同一类型机械数量）';

--------------------------
-- 4. ALG_SILO_BASE（筒仓基础数据表）
--------------------------
COMMENT ON COLUMN ALG_SILO_BASE.ID IS '筒仓唯一标识符';
COMMENT ON COLUMN ALG_SILO_BASE.SILO_NO IS '筒仓编号（如：SILO-101）';
COMMENT ON COLUMN ALG_SILO_BASE.BELONG_PERIOD IS '所属运营期（如：三期工程）';
COMMENT ON COLUMN ALG_SILO_BASE.TOTAL_CAPACITY IS '筒仓设计总容量（单位：吨）';
COMMENT ON COLUMN ALG_SILO_BASE.REAL_TIME_CAPACITY IS '当前实时存储量（单位：吨）';
COMMENT ON COLUMN ALG_SILO_BASE.OCCUPIED_CAPACITY IS '已占用容量（单位：吨）';
COMMENT ON COLUMN ALG_SILO_BASE.AVAILABLE_CAPACITY IS '剩余可用容量（单位：吨）';

--------------------------
-- 5. ALG_SILO_REALTIME（筒仓实时数据表）
--------------------------
COMMENT ON COLUMN ALG_SILO_REALTIME.ID IS '实时记录唯一ID';
COMMENT ON COLUMN ALG_SILO_REALTIME.SILO_NO IS '关联的筒仓编号';
COMMENT ON COLUMN ALG_SILO_REALTIME.STAT_DATE IS '数据统计时间点（精确到秒）';
COMMENT ON COLUMN ALG_SILO_REALTIME.CARGO_NAME IS '当前存储货物名称';
COMMENT ON COLUMN ALG_SILO_REALTIME.CARGO_ORIGIN IS '货物原产地（如：澳大利亚）';
COMMENT ON COLUMN ALG_SILO_REALTIME.CARGO_OWNER IS '货物所有权归属方';
COMMENT ON COLUMN ALG_SILO_REALTIME.TOTAL_CAPACITY IS '筒仓总容量（单位：吨）';
COMMENT ON COLUMN ALG_SILO_REALTIME.HANDOVER_AMOUNT IS '交班时记录的货物数量';
COMMENT ON COLUMN ALG_SILO_REALTIME.TAKEOVER_AMOUNT IS '接班时接收的货物数量';
COMMENT ON COLUMN ALG_SILO_REALTIME.INBOUND_QTY IS '本班次进仓货物量（单位：吨）';
COMMENT ON COLUMN ALG_SILO_REALTIME.OUTBOUND_QTY IS '本班次出仓货物量（单位：吨）';
COMMENT ON COLUMN ALG_SILO_REALTIME.CLEARANCE_DIFF IS '清仓时发现的货差（单位：吨）';

--------------------------
-- 6. ALG_SILO_BERTH_REL（筒仓泊位关系表）
--------------------------
COMMENT ON COLUMN ALG_SILO_BERTH_REL.SILO_NO IS '关联的筒仓编号';
COMMENT ON COLUMN ALG_SILO_BERTH_REL.BERTH_NO IS '关联的泊位编号';

--------------------------
-- 7. ALG_SHIP_SILO_ARRANGE（船舶筒仓安排表）
--------------------------
COMMENT ON COLUMN ALG_SHIP_SILO_ARRANGE.VOYAGE_NO IS '关联的航次号';
COMMENT ON COLUMN ALG_SHIP_SILO_ARRANGE.SILO_NO IS '分配的筒仓编号';

--------------------------
-- 8. ALG_BELT_PROCESS（皮带流程表）
--------------------------
COMMENT ON COLUMN ALG_BELT_PROCESS.ID IS '流程配置唯一ID';
COMMENT ON COLUMN ALG_BELT_PROCESS.PROCESS_NO IS '皮带流程编号（如：BELT-01）';
COMMENT ON COLUMN ALG_BELT_PROCESS.PROCESS_NAME IS '皮带流程名称（如：煤炭运输线）';
COMMENT ON COLUMN ALG_BELT_PROCESS.FLOW_RATE IS '设计流量（单位：吨/小时）';
COMMENT ON COLUMN ALG_BELT_PROCESS.PROCESS_ATTR IS '流程属性（如：双向运输/单向运输）';
COMMENT ON COLUMN ALG_BELT_PROCESS.HEAD_DEVICE IS '流程头部设备编号';
COMMENT ON COLUMN ALG_BELT_PROCESS.HEAD_LINK_SHORE IS '头部设备连接的岸基设备';
COMMENT ON COLUMN ALG_BELT_PROCESS.HEAD_LINK_SILO IS '头部设备关联的筒仓编号';
COMMENT ON COLUMN ALG_BELT_PROCESS.MID_DEVICE IS '流程中间设备编号';
COMMENT ON COLUMN ALG_BELT_PROCESS.TAIL_DEVICE IS '流程尾部设备编号';
COMMENT ON COLUMN ALG_BELT_PROCESS.TAIL_LINK_SILO IS '尾部设备关联的筒仓编号';

--------------------------
-- 9. ALG_BELT_PROCESS_OCC（皮带占用表）
--------------------------
COMMENT ON COLUMN ALG_BELT_PROCESS_OCC.VOYAGE_NO IS '关联的航次号';
COMMENT ON COLUMN ALG_BELT_PROCESS_OCC.OCCUPIED_PROCESS_NO IS '被占用的皮带流程编号';

--------------------------
-- 10. ALG_YARD（堆场基础表）
--------------------------
COMMENT ON COLUMN ALG_YARD.ID IS '堆场唯一标识符';
COMMENT ON COLUMN ALG_YARD.YARD_NAME IS '堆场名称（如：三期散货堆场）';
COMMENT ON COLUMN ALG_YARD.YARD_NO IS '堆场编号（如：YARD-03）';
COMMENT ON COLUMN ALG_YARD.BELONG_ZONE IS '所属区域（如：北区/南区）';
COMMENT ON COLUMN ALG_YARD.TOTAL_CAPACITY IS '堆场设计总容量（单位：吨）';
COMMENT ON COLUMN ALG_YARD.REAL_TIME_CAPACITY IS '当前堆存货量（单位：吨）';
COMMENT ON COLUMN ALG_YARD.OCCUPIED_CAPACITY IS '已占用容量（单位：吨）';
COMMENT ON COLUMN ALG_YARD.AVAILABLE_CAPACITY IS '剩余可用容量（单位：吨）';

--------------------------
-- 11. ALG_YARD_BERTH_REL（堆场泊位关系表）
--------------------------
COMMENT ON COLUMN ALG_YARD_BERTH_REL.YARD_NO IS '关联的堆场编号';
COMMENT ON COLUMN ALG_YARD_BERTH_REL.BERTH_NO IS '关联的泊位编号';

--------------------------
-- 12. ALG_YARD_CARGO_REL（堆场货种表）
--------------------------
COMMENT ON COLUMN ALG_YARD_CARGO_REL.YARD_NO IS '关联的堆场编号';
COMMENT ON COLUMN ALG_YARD_CARGO_REL.CARGO_TYPE_NAME IS '允许堆放的货种大类（如：煤炭/铁矿）';

--------------------------
-- 13. ALG_YARD_REALTIME（堆场实时表）
--------------------------
COMMENT ON COLUMN ALG_YARD_REALTIME.ID IS '实时记录唯一ID';
COMMENT ON COLUMN ALG_YARD_REALTIME.YARD_NO IS '关联的堆场编号';
COMMENT ON COLUMN ALG_YARD_REALTIME.STAT_DATE IS '数据统计时间点（精确到秒）';
COMMENT ON COLUMN ALG_YARD_REALTIME.CARGO_TYPE IS '当前堆存货种类型';
COMMENT ON COLUMN ALG_YARD_REALTIME.CARGO_ORIGIN IS '货物原产地';
COMMENT ON COLUMN ALG_YARD_REALTIME.CARGO_OWNER IS '货主名称';
COMMENT ON COLUMN ALG_YARD_REALTIME.STORAGE_CAPACITY IS '堆场总容量（单位：吨）';
COMMENT ON COLUMN ALG_YARD_REALTIME.BELONG_AREA IS '所属区域（如：三期北区）';
COMMENT ON COLUMN ALG_YARD_REALTIME.YESTERDAY_STOCK IS '昨日库存数量（单位：吨）';
COMMENT ON COLUMN ALG_YARD_REALTIME.INBOUND_TIME IS '最近一次入库时间';
COMMENT ON COLUMN ALG_YARD_REALTIME.YESTERDAY_INBOUND IS '上昼夜入库量（单位：吨）';
COMMENT ON COLUMN ALG_YARD_REALTIME.YESTERDAY_TRANSFER_IN IS '上昼夜倒运入库量（单位：吨）';
COMMENT ON COLUMN ALG_YARD_REALTIME.INVENTORY IS '当前盘点数量（单位：吨）';
COMMENT ON COLUMN ALG_YARD_REALTIME.YESTERDAY_OUTBOUND IS '上昼夜出库量（单位：吨）';
COMMENT ON COLUMN ALG_YARD_REALTIME.YESTERDAY_TRANSFER_OUT IS '上昼夜倒运出库量（单位：吨）';
COMMENT ON COLUMN ALG_YARD_REALTIME.CURRENT_STOCK IS '当前实际库存量（单位：吨）';

--------------------------
-- 14. ALG_SHIP_YARD_ARRANGE（船舶堆场安排表）
--------------------------
COMMENT ON COLUMN ALG_SHIP_YARD_ARRANGE.VOYAGE_NO IS '关联的航次号';
COMMENT ON COLUMN ALG_SHIP_YARD_ARRANGE.YARD_NAME IS '分配的堆场名称';
COMMENT ON COLUMN ALG_SHIP_YARD_ARRANGE.YARD_NO IS '分配的堆场编号';



ALTER TABLE "APSYL"."ALG_SHIP_FORECAST"
    ADD ("CARGO_WHEREABOUTS" NUMBER(1));

COMMENT ON COLUMN "ALG_SHIP_FORECAST"."CARGO_WHEREABOUTS" IS '装卸要求（1直取|2存放）';

ALTER TABLE "APSYL"."ALG_SHIP_FORECAST"
    ADD ("CARGO_EVACUATION" NUMBER(1));

COMMENT ON COLUMN "ALG_SHIP_FORECAST"."CARGO_EVACUATION" IS '疏港方式';


ALTER TABLE "APSYL"."ALG_YARD_BERTH_REL"
    ADD ("PRIORITY" NUMBER(4));

COMMENT ON COLUMN "APSYL"."ALG_YARD_BERTH_REL"."PRIORITY" IS '优先级，越大越高';

ALTER TABLE "APSYL"."ALG_SILO_BERTH_REL"
    ADD ("PRIORITY" NUMBER(4));

COMMENT ON COLUMN "APSYL"."ALG_SILO_BERTH_REL"."PRIORITY" IS '优先级，越大越高'




ALTER TABLE "APSYL"."ALG_YARD"
    ADD ("YARD_TYPE" NUMBER(1))

COMMENT ON COLUMN "APSYL"."ALG_YARD"."YARD_TYPE" IS '库场类型 1库房 2堆场'


CREATE TABLE ALG_SHIP_WORK_PLAN_TEMP
(
    ID                VARCHAR2(50) PRIMARY KEY, -- 主表唯一ID
    VOYAGE_NO         VARCHAR2(50),             -- 航次号申报唯一ID
    BERTH_NO          VARCHAR2(20),             -- 泊位号
    BERTH_DIRECTION   VARCHAR2(100),            -- 靠泊要求（着岸）
    HEAD_BOLLARD_ID   VARCHAR2(20),             -- 首榄桩编号
    TAIL_BOLLARD_ID   VARCHAR2(20),             -- 尾榄桩编号
    SHIP_NAME         VARCHAR2(100),            -- 船舶名称
    CABIN_NUM         NUMBER(4),                -- 货舱数量
    CARRY_WEIGHT      NUMBER(12,2),             -- 载货总吨数
    SHIP_LENGTH       NUMBER(8,2),              -- 船舶长度（米）
    DRAFT             NUMBER(6,2),              -- 船舶吃水深度（米）
    CARGO_NAME        VARCHAR2(100),            -- 货物名称
    CARGO_OWNER       VARCHAR2(100),            -- 货物所有者
    AGENT             VARCHAR2(100),            -- 代理公司
    WORK_CONTENT             VARCHAR2(255),            --作业内容
    PLAN_TIME   TIMESTAMP,                -- 计划时间
    ALGORITHM_STATE  NUMBER(4),                -- 计划时间
    PLAN_BERTH_TIME   TIMESTAMP,                -- 计划靠泊时间
    BERTH_TIME        TIMESTAMP,                -- 实际靠泊时间
    PLAN_FINISH_TIME  TIMESTAMP,                -- 计划完成时间
    THROUGHPUT_WEIGHT NUMBER(12,2),             -- 吞吐总吨数
    REMAINING_WEIGHT  NUMBER(12,2)              -- 剩余吨数
);
COMMENT
ON TABLE ALG_SHIP_WORK_PLAN_TEMP IS '船舶作业计划主表，记录船舶作业核心信息';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.ID IS '主表唯一标识符';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.VOYAGE_NO IS '航次号（唯一标识申报航次）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.BERTH_NO IS '船舶停靠的泊位编号';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.BERTH_DIRECTION IS '靠泊方向描述（如：左舷靠泊、右舷靠泊）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.HEAD_BOLLARD_ID IS '船舶首部固定缆绳的缆桩编号';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.TAIL_BOLLARD_ID IS '船舶尾部固定缆绳的缆桩编号';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.SHIP_NAME IS '船舶注册名称';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.CABIN_NUM IS '船舶货舱总数';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.CARRY_WEIGHT IS '船舶载货总重量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.SHIP_LENGTH IS '船舶总长度（单位：米）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.DRAFT IS '船舶吃水深度（单位：米）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.CARGO_NAME IS '装载货物名称';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.CARGO_OWNER IS '货物所有权归属方';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.AGENT IS '船舶代理公司名称';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.PLAN_BERTH_TIME IS '计划靠泊时间（精确到秒）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.BERTH_TIME IS '实际靠泊完成时间';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.PLAN_FINISH_TIME IS '计划作业完成时间';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.THROUGHPUT_WEIGHT IS '已完成货物吞吐量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.REMAINING_WEIGHT IS '剩余待作业货物量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.WORK_CONTENT IS '作业内容';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.PLAN_TIME IS '作业时间';
COMMENT ON COLUMN ALG_SHIP_WORK_PLAN_TEMP.ALGORITHM_STATE IS '算法状态';

--------------------------
-- 2. 作业计划班次表
--------------------------
CREATE TABLE ALG_SHIP_WORK_SHIFT_TEMP
(
    ID                VARCHAR2(50) PRIMARY KEY, -- 班次唯一ID
    SHIP_WORK_PLAN_ID VARCHAR2(50),             -- 关联主表ID
    SHIFT_TYPE        NUMBER(1),                -- 班次类型（1=夜班，2=昼班）
    WORKLOAD          NUMBER(10,2),             -- 班次作业量（吨）
    START_TIME        TIMESTAMP,                -- 班次开始时间
    END_TIME          TIMESTAMP,                -- 班次结束时间
    WORK_HOURS        NUMBER(6,2),              -- 作业时长（小时）
    MECH_EFFICIENCY   NUMBER(5,2),              -- 总机械效率（百分比）
    MANPOWER_ARRANGE  VARCHAR2(200)             -- 人力安排描述

);
COMMENT
ON TABLE ALG_SHIP_WORK_SHIFT_TEMP IS '作业班次明细表，记录每个班次的具体作业计划';

COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.ID IS '班次唯一标识符';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.SHIP_WORK_PLAN_ID IS '关联的船舶作业计划ID';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.SHIFT_TYPE IS '班次类型（1=夜班，2=昼班）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.WORKLOAD IS '本班次计划作业量（单位：吨）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.START_TIME IS '班次开始时间（精确到秒）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.END_TIME IS '班次结束时间（精确到秒）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.WORK_HOURS IS '实际作业时长（单位：小时）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.MECH_EFFICIENCY IS '机械综合效率（百分比，如80.5）';
COMMENT ON COLUMN ALG_SHIP_WORK_SHIFT_TEMP.MANPOWER_ARRANGE IS '人力配置详情（如：装卸工10人）';