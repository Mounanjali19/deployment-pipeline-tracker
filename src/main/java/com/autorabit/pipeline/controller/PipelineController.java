package com.autorabit.pipeline.controller;

import com.autorabit.pipeline.dto.*;
import com.autorabit.pipeline.entity.CodeChange;
import com.autorabit.pipeline.service.PipelineService;
import org.springframework.web.bind.annotation.*;
import com.autorabit.pipeline.entity.DeploymentLog;
import java.util.List;

@RestController
@RequestMapping("/api/changes")
public class PipelineController {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping
    public CodeChange submitChange(@RequestBody CreateChangeRequest request) {
        return pipelineService.submitChange(request);
    }

    @GetMapping("/{id}")
    public CodeChange getChange(@PathVariable Long id) {
        return pipelineService.getChangeById(id);
    }

    @GetMapping("/dashboard")
    public List<CodeChange> getPendingChanges() {
        return pipelineService.getPendingChanges();
    }

    @PostMapping("/{id}/scan")
    public String scanChange(@PathVariable Long id,
                             @RequestBody ScanRequest request) {

        return pipelineService.scanChange(id, request);
    }

    @PostMapping("/{id}/approve")
    public String approveChange(@PathVariable Long id,
                                @RequestBody ApprovalRequest request) {

        return pipelineService.approveChange(id, request);
    }

    @PostMapping("/{id}/deploy")
    public String deployChange(@PathVariable Long id) {

        return pipelineService.deployChange(id);
    }

    @PostMapping("/{id}/rollback")
    public String rollbackChange(@PathVariable Long id,
                                 @RequestBody RollbackRequest request) {

        return pipelineService.rollbackChange(id, request);
    }
    @GetMapping("/{id}/history")
    public List<DeploymentLog> getDeploymentHistory(@PathVariable Long id) {

        return pipelineService.getDeploymentHistory(id);
    }
}
