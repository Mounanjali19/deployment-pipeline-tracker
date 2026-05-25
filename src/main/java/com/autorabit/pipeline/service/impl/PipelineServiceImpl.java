package com.autorabit.pipeline.service.impl;

import com.autorabit.pipeline.dto.*;
import com.autorabit.pipeline.entity.CodeChange;
import com.autorabit.pipeline.entity.DeploymentLog;
import com.autorabit.pipeline.entity.Developer;
import com.autorabit.pipeline.entity.ScanResult;
import com.autorabit.pipeline.enums.ChangeStatus;
import com.autorabit.pipeline.enums.Role;
import com.autorabit.pipeline.exception.InvalidStateException;
import com.autorabit.pipeline.exception.ResourceNotFoundException;
import com.autorabit.pipeline.exception.UnauthorizedActionException;
import com.autorabit.pipeline.repository.CodeChangeRepository;
import com.autorabit.pipeline.repository.DeploymentLogRepository;
import com.autorabit.pipeline.repository.DeveloperRepository;
import com.autorabit.pipeline.repository.ScanResultRepository;
import com.autorabit.pipeline.service.PipelineService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PipelineServiceImpl implements PipelineService {

    private final CodeChangeRepository codeChangeRepository;
    private final DeveloperRepository developerRepository;
    private final ScanResultRepository scanResultRepository;
    private final DeploymentLogRepository deploymentLogRepository;

    public PipelineServiceImpl(CodeChangeRepository codeChangeRepository,
                               DeveloperRepository developerRepository,
                               ScanResultRepository scanResultRepository,
                               DeploymentLogRepository deploymentLogRepository) {

        this.codeChangeRepository = codeChangeRepository;
        this.developerRepository = developerRepository;
        this.scanResultRepository = scanResultRepository;
        this.deploymentLogRepository = deploymentLogRepository;
    }

    @Override
    public CodeChange submitChange(CreateChangeRequest request) {

        Developer developer = developerRepository.findById(request.getDeveloperId())
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));

        CodeChange codeChange = new CodeChange();

        codeChange.setTitle(request.getTitle());
        codeChange.setDescription(request.getDescription());
        codeChange.setRepositoryName(request.getRepositoryName());
        codeChange.setSubmittedAt(LocalDateTime.now());
        codeChange.setStatus(ChangeStatus.SUBMITTED);
        codeChange.setDeveloper(developer);

        return codeChangeRepository.save(codeChange);
    }

    @Override
    public CodeChange getChangeById(Long id) {

        return codeChangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Code change not found"));
    }

    @Override
    public List<CodeChange> getPendingChanges() {

        return codeChangeRepository.findByStatus(ChangeStatus.SUBMITTED);
    }

    @Override
    public String scanChange(Long changeId, ScanRequest request) {

        CodeChange codeChange = getChangeById(changeId);

        if (codeChange.getStatus() != ChangeStatus.SUBMITTED) {
            throw new InvalidStateException("Only submitted changes can be scanned");
        }

        codeChange.setStatus(ChangeStatus.SCANNING);
        codeChangeRepository.save(codeChange);

        ScanResult scanResult = new ScanResult();
        scanResult.setPassed(request.isPassed());
        scanResult.setIssuesFound(request.getIssuesFound());
        scanResult.setCodeChange(codeChange);

        scanResultRepository.save(scanResult);

        codeChange.setStatus(ChangeStatus.REVIEWED);
        codeChangeRepository.save(codeChange);

        return "Scan completed successfully";
    }

    @Override
    public String approveChange(Long changeId, ApprovalRequest request) {

        CodeChange codeChange = getChangeById(changeId);

        Developer reviewer = developerRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));

        if (reviewer.getRole() != Role.REVIEWER &&
                reviewer.getRole() != Role.ADMIN) {

            throw new UnauthorizedActionException("Only reviewer or admin can approve");
        }

        if (codeChange.getDeveloper().getId().equals(reviewer.getId())) {
            throw new UnauthorizedActionException("Developer cannot approve own code");
        }

        if (codeChange.getStatus() != ChangeStatus.REVIEWED) {
            throw new InvalidStateException("Code must be reviewed before approval");
        }

        ScanResult scanResult = scanResultRepository
                .findTopByCodeChangeIdOrderByIdDesc(changeId);

        if (scanResult != null && !scanResult.isPassed()) {
            throw new InvalidStateException("Cannot approve failed scan");
        }
        codeChange.setStatus(ChangeStatus.APPROVED);
        codeChangeRepository.save(codeChange);

        return "Code approved successfully";
    }

    @Override
    public String deployChange(Long changeId) {

        CodeChange codeChange = getChangeById(changeId);

        if (codeChange.getStatus() != ChangeStatus.APPROVED) {
            throw new InvalidStateException("Only approved code can be deployed");
        }

        DeploymentLog deploymentLog = new DeploymentLog();

        deploymentLog.setCodeChange(codeChange);
        deploymentLog.setVersionNumber((int) (deploymentLogRepository.count() + 1));
        deploymentLog.setRolledBack(false);
        deploymentLog.setDeployedAt(LocalDateTime.now());

        deploymentLogRepository.save(deploymentLog);

        codeChange.setStatus(ChangeStatus.DEPLOYED);
        codeChangeRepository.save(codeChange);

        return "Deployment successful";
    }

    @Override
    public String rollbackChange(Long changeId, RollbackRequest request) {

        CodeChange codeChange = getChangeById(changeId);

        Developer admin = developerRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("Only admin can rollback");
        }

        if (codeChange.getStatus() != ChangeStatus.DEPLOYED) {
            throw new InvalidStateException("Only deployed changes can rollback");
        }

        DeploymentLog deploymentLog = new DeploymentLog();

        deploymentLog.setCodeChange(codeChange);
        deploymentLog.setVersionNumber((int) (deploymentLogRepository.count() + 1));
        deploymentLog.setRolledBack(true);
        deploymentLog.setDeployedAt(LocalDateTime.now());

        deploymentLogRepository.save(deploymentLog);

        codeChange.setStatus(ChangeStatus.ROLLED_BACK);
        codeChangeRepository.save(codeChange);

        return "Rollback successful";


    }
    @Override
    public CodeChange scanChange(Long id) {

        CodeChange codeChange = codeChangeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Code change not found"));

        codeChange.setStatus(ChangeStatus.SCANNING);

        ScanResult scanResult = new ScanResult();

        scanResult.setCodeChange(codeChange);

        if (codeChange.getDescription().toLowerCase().contains("null")) {
            scanResult.setPassed(false);
            scanResult.setIssuesFound("Possible null pointer issue detected");
        } else {
            scanResult.setPassed(true);
            scanResult.setIssuesFound("No issues found");
            codeChange.setStatus(ChangeStatus.REVIEWED);
        }

        scanResultRepository.save(scanResult);

        return codeChangeRepository.save(codeChange);
    }
    @Override
    public List<DeploymentLog> getDeploymentHistory(Long changeId) {

        getChangeById(changeId);

        return deploymentLogRepository.findByCodeChangeId(changeId);
    }
}
