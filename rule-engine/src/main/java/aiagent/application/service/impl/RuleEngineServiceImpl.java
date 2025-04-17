package aiagent.application.service.impl;


import aiagent.application.dto.CreditEvaluationRequest;
import aiagent.application.dto.CreditEvaluationResponse;
import aiagent.application.service.RuleEngineService;
import aiagent.domain.valueobject.ApprovalDecision;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleEngineServiceImpl implements RuleEngineService {

    // 模拟规则版本
    private static final Map<String, RuleSet> RULE_VERSIONS = new HashMap<>();
    
    static {
        // 初始化规则版本
        RULE_VERSIONS.put("v1", new RuleSet("v1", Arrays.asList(
            new Rule("信用分数规则", data -> {
                int creditScore = (int) data.get("creditScore");
                return creditScore >= 700 ? "通过" : (creditScore >= 650 ? "需要审核" : "拒绝");
            }),
            new Rule("收入规则", data -> {
                double income = (double) data.get("monthlyIncome");
                return income >= 10000 ? "通过" : (income >= 5000 ? "需要审核" : "拒绝");
            })
        )));
        
        RULE_VERSIONS.put("v2", new RuleSet("v2", Arrays.asList(
            new Rule("信用分数规则", data -> {
                int creditScore = (int) data.get("creditScore");
                return creditScore >= 680 ? "通过" : (creditScore >= 620 ? "需要审核" : "拒绝");
            }),
            new Rule("收入规则", data -> {
                double income = (double) data.get("monthlyIncome");
                return income >= 8000 ? "通过" : (income >= 4000 ? "需要审核" : "拒绝");
            }),
            new Rule("负债比规则", data -> {
                double debtRatio = (double) data.get("debtToIncomeRatio");
                return debtRatio <= 0.4 ? "通过" : (debtRatio <= 0.6 ? "需要审核" : "拒绝");
            })
        )));
    }

    @Override
    public CreditEvaluationResponse evaluateCredit(CreditEvaluationRequest request) {
        // 获取规则版本
        String ruleVersion = request.getRuleVersion();
        if (ruleVersion == null || !RULE_VERSIONS.containsKey(ruleVersion)) {
            ruleVersion = "v1"; // 默认使用v1版本
        }
        
        RuleSet ruleSet = RULE_VERSIONS.get(ruleVersion);
        Map<String, Object> inputData = request.getInputData();
        
        // 执行规则评估
        List<Map<String, String>> ruleLogs = new ArrayList<>();
        List<String> riskFactors = new ArrayList<>();
        int totalScore = 0;
        int passCount = 0;
        
        for (Rule rule : ruleSet.getRules()) {
            String result = rule.evaluate(inputData);
            
            Map<String, String> ruleLog = new HashMap<>();
            ruleLog.put("ruleName", rule.getName());
            ruleLog.put("result", result);
            ruleLogs.add(ruleLog);
            
            if ("通过".equals(result)) {
                passCount++;
                totalScore += 100;
            } else if ("需要审核".equals(result)) {
                totalScore += 50;
                riskFactors.add(rule.getName());
            } else {
                riskFactors.add(rule.getName());
            }
        }
        
        // 计算最终分数
        int finalScore = ruleSet.getRules().isEmpty() ? 0 : totalScore / ruleSet.getRules().size();
        
        // 确定最终决策
        String decision;
        if (passCount == ruleSet.getRules().size()) {
            decision = ApprovalDecision.APPROVED.getCode();
        } else if (passCount == 0) {
            decision = ApprovalDecision.REJECTED.getCode();
        } else {
            decision = ApprovalDecision.PENDING_REVIEW.getCode();
        }
        
        return new CreditEvaluationResponse(decision, finalScore, riskFactors, ruleLogs);
    }
    
    // 规则集类
    private static class RuleSet {
        private final String version;
        private final List<Rule> rules;
        
        public RuleSet(String version, List<Rule> rules) {
            this.version = version;
            this.rules = rules;
        }
        
        public List<Rule> getRules() {
            return rules;
        }
    }
    
    // 规则类
    private static class Rule {
        private final String name;
        private final RuleEvaluator evaluator;
        
        public Rule(String name, RuleEvaluator evaluator) {
            this.name = name;
            this.evaluator = evaluator;
        }
        
        public String getName() {
            return name;
        }
        
        public String evaluate(Map<String, Object> data) {
            return evaluator.evaluate(data);
        }
    }
    
    // 规则评估器接口
    private interface RuleEvaluator {
        String evaluate(Map<String, Object> data);
    }
}