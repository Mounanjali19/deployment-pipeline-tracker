package com.autorabit.pipeline.service;

import com.autorabit.pipeline.dto.*;
import com.autorabit.pipeline.entity.CodeChange;
import com.autorabit.pipeline.entity.DeploymentLog;
import java.util.List;

public interface PipelineService {

    CodeChange submitChange(CreateChangeRequest request);

    CodeChange scanChange(Long id);

    CodeChange getChangeById(Long id);

    List<CodeChange> getPendingChanges();

    String scanChange(Long changeId, ScanRequest request);

    String approveChange(Long changeId, ApprovalRequest request);

    String deployChange(Long changeId);

    String rollbackChange(Long changeId, RollbackRequest request);

    List<DeploymentLog> getDeploymentHistory(Long changeId);
}