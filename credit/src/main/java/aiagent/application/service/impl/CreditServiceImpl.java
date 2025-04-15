package aiagent.application.service.impl;

import aiagent.application.dto.CreditQueryResponse;
import aiagent.application.dto.CreditQueryResponse.LoanHistoryDTO;
import aiagent.application.service.CreditService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CreditServiceImpl implements CreditService {

    // 模拟用户信用数据存储
    // 实际数据来源于外部征信系统
    private final Map<String, UserCreditData> creditDataStore = new HashMap<>();
    private final Random random = new Random();

    public CreditServiceImpl() {
        // 初始化一些模拟数据
        initializeMockData();
    }

    @Override
    public CreditQueryResponse queryCreditData(String userId) {
        // 检查是否有缓存数据
        UserCreditData userData = creditDataStore.get(userId);

        // 如果没有找到用户数据，创建一个随机的信用数据
        if (userData == null) {
            userData = generateRandomCreditData(userId);
            creditDataStore.put(userId, userData);
            return new CreditQueryResponse(userData.creditScore, userData.loanHistory, false // 不是缓存数据
            );
        }

        // 返回已有数据
        return new CreditQueryResponse(userData.creditScore, userData.loanHistory, true // 是缓存数据
        );
    }

    private void initializeMockData() {
        // 添加一些模拟用户数据
        String userId1 = "user123";
        List<LoanHistoryDTO> history1 = new ArrayList<>();
        history1.add(new LoanHistoryDTO(50000, "repaid"));
        history1.add(new LoanHistoryDTO(10000, "active"));
        creditDataStore.put(userId1, new UserCreditData(720, history1));

        String userId2 = "user456";
        List<LoanHistoryDTO> history2 = new ArrayList<>();
        history2.add(new LoanHistoryDTO(100000, "overdue"));
        history2.add(new LoanHistoryDTO(30000, "repaid"));
        creditDataStore.put(userId2, new UserCreditData(650, history2));
    }

    private UserCreditData generateRandomCreditData(String userId) {
        // 生成随机信用分数 (550-850)
        int creditScore = 550 + random.nextInt(301);

        // 生成随机贷款历史
        List<LoanHistoryDTO> loanHistory = new ArrayList<>();
        int loanCount = 1 + random.nextInt(3); // 1-3笔贷款

        String[] statuses = {"repaid", "active", "overdue"};
        for (int i = 0; i < loanCount; i++) {
            double amount = 10000 + random.nextInt(90000);
            String status = statuses[random.nextInt(statuses.length)];
            loanHistory.add(new LoanHistoryDTO(amount, status));
        }

        return new UserCreditData(creditScore, loanHistory);
    }

    // 内部类，用于存储用户信用数据
    private record UserCreditData(int creditScore, List<LoanHistoryDTO> loanHistory) {
    }
}