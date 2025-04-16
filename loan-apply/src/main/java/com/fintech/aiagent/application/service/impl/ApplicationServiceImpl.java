package com.fintech.aiagent.application.service.impl;

import com.fintech.aiagent.application.dto.ApplicationSubmitRequest;
import com.fintech.aiagent.application.dto.ApplicationSubmitResponse;
import com.fintech.aiagent.application.service.ApplicationService;
import com.fintech.aiagent.domain.credit.aggregate.CreditApplicationAggregate;
import com.fintech.aiagent.domain.credit.entity.Document;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service("creditApplicationServiceImpl")  // 修改Bean名称
public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public ApplicationSubmitResponse submitApplication(ApplicationSubmitRequest request) {
        // 1. 创建信贷申请聚合根
        CreditApplicationAggregate applicationAggregate = new CreditApplicationAggregate(
                new CustomerId(request.getUserId()),
                request.getApplicationType()
        );
        
        // 2. 处理上传的文件
        List<String> missingFields = new ArrayList<>();
        Map<String, Object> ocrResults = new HashMap<>();
        
        // 检查必要文件是否上传
        boolean hasIdCard = false;
        boolean hasBankStatement = false;
        
        for (MultipartFile file : request.getFiles()) {
            String originalFilename = file.getOriginalFilename();
            String documentType = determineDocumentType(originalFilename);
            
            // 创建文档实体
            Document document = new Document(
                    UUID.randomUUID().toString(),
                    documentType,
                    "/storage/uploads/" + UUID.randomUUID().toString(),
                    originalFilename,
                    file.getContentType(),
                    file.getSize()
            );
            
            // 添加到申请中
            applicationAggregate.uploadDocument(document);
            
            // 更新文件类型检查
            if ("id_card".equals(documentType)) {
                hasIdCard = true;
                // 模拟OCR结果
                Map<String, String> idCardInfo = new HashMap<>();
                idCardInfo.put("name", "张三");
                idCardInfo.put("idNumber", "310***");
                ocrResults.put("idCard", idCardInfo);
            } else if ("bank_statement".equals(documentType)) {
                hasBankStatement = true;
            } else if ("income_proof".equals(documentType)) {
                // 模拟OCR结果
                Map<String, Object> incomeInfo = new HashMap<>();
                incomeInfo.put("monthlyIncome", 15000);
                ocrResults.put("incomeProof", incomeInfo);
            }
        }
        
        // 检查缺失的文件
        if (!hasIdCard) {
            missingFields.add("id_card");
        }
        if (!hasBankStatement) {
            missingFields.add("bank_statement");
        }
        
        // 3. 返回响应
        return new ApplicationSubmitResponse(
                applicationAggregate.getApplicationId().getId(),
                missingFields,
                ocrResults
        );
    }
    
    private String determineDocumentType(String filename) {
        if (filename == null) return "unknown";
        
        filename = filename.toLowerCase();
        if (filename.contains("idcard") || filename.contains("id_card") || filename.contains("身份证")) {
            return "id_card";
        } else if (filename.contains("bank") || filename.contains("statement") || filename.contains("流水")) {
            return "bank_statement";
        } else if (filename.contains("income") || filename.contains("salary") || filename.contains("收入")) {
            return "income_proof";
        }
        
        return "other";
    }
}