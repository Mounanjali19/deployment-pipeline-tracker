package com.autorabit.pipeline.service;

import com.autorabit.pipeline.dto.ApprovalRequest;
import com.autorabit.pipeline.entity.CodeChange;
import com.autorabit.pipeline.entity.Developer;
import com.autorabit.pipeline.entity.ScanResult;
import com.autorabit.pipeline.enums.ChangeStatus;
import com.autorabit.pipeline.enums.Role;
import com.autorabit.pipeline.exception.InvalidStateException;
import com.autorabit.pipeline.repository.CodeChangeRepository;
import com.autorabit.pipeline.repository.DeploymentLogRepository;
import com.autorabit.pipeline.repository.DeveloperRepository;
import com.autorabit.pipeline.repository.ScanResultRepository;
import com.autorabit.pipeline.service.impl.PipelineServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class PipelineServiceImplTest {

    @Mock
    private CodeChangeRepository codeChangeRepository;

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private ScanResultRepository scanResultRepository;

    @Mock
    private DeploymentLogRepository deploymentLogRepository;

    @InjectMocks
    private PipelineServiceImpl pipelineService;

    public PipelineServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldBlockApprovalWhenScanFails() {

        CodeChange codeChange = new CodeChange();
        codeChange.setId(1L);
        codeChange.setStatus(ChangeStatus.REVIEWED);

        Developer reviewer = new Developer();
        reviewer.setId(2L);
        reviewer.setRole(Role.REVIEWER);

        ScanResult scanResult = new ScanResult();
        scanResult.setPassed(false);

        ApprovalRequest request = new ApprovalRequest();
        request.setReviewerId(2L);

        when(codeChangeRepository.findById(1L))
                .thenReturn(Optional.of(codeChange));

        when(developerRepository.findById(2L))
                .thenReturn(Optional.of(reviewer));

        when(scanResultRepository
                .findTopByCodeChangeIdOrderByIdDesc(1L))
                .thenReturn(scanResult);

        assertThrows(
                InvalidStateException.class,
                () -> pipelineService.approveChange(1L, request)
        );
    }
    @Test
    void developerCannotApproveOwnCode() {

        CodeChange codeChange = new CodeChange();
        codeChange.setId(1L);
        codeChange.setStatus(ChangeStatus.REVIEWED);

        Developer developer = new Developer();
        developer.setId(1L);
        developer.setRole(Role.REVIEWER);

        codeChange.setDeveloper(developer);

        ApprovalRequest request = new ApprovalRequest();
        request.setReviewerId(1L);

        when(codeChangeRepository.findById(1L))
                .thenReturn(Optional.of(codeChange));

        when(developerRepository.findById(1L))
                .thenReturn(Optional.of(developer));

        assertThrows(
                Exception.class,
                () -> pipelineService.approveChange(1L, request)
        );
    }

    @Test
    void shouldBlockDeploymentIfNotApproved() {

        CodeChange codeChange = new CodeChange();
        codeChange.setId(1L);
        codeChange.setStatus(ChangeStatus.REVIEWED);

        when(codeChangeRepository.findById(1L))
                .thenReturn(Optional.of(codeChange));

        assertThrows(
                InvalidStateException.class,
                () -> pipelineService.deployChange(1L)
        );
    }
}