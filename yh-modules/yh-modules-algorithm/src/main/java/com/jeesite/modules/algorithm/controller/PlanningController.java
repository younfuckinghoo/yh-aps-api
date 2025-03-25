package com.jeesite.modules.algorithm.controller;

import com.jeesite.common.base.R;
import com.jeesite.modules.algorithm.service.IPlanningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "船舶计划")
@RequestMapping("plan")
@RestController
public class PlanningController {

//    public PlanningController(IPlanningService iPlanningService) {
//        this.iPlanningService = iPlanningService;
//    }

//    private final IPlanningService iPlanningService;
//
//    @Operation(summary = "长期计划排产")
//    @ApiResponse(responseCode = "200", description = "成功",
//            content = {@Content(mediaType = "application/json")})
//    @PostMapping("schedule")
//    public R scheduleShipPlan(){
//        return this.iPlanningService.scheduleShipPlan();
//    }

}
