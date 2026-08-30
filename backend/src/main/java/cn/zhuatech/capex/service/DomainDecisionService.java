/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.capex.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class DomainDecisionService {
 public DecisionResult assess(DecisionRequest request) { if(request.approvedAmount()>request.requestedAmount())throw new IllegalArgumentException("批准金额不能大于申请金额");int score=100;List<String> actions=new ArrayList<>();if(request.requestedAmount()>request.budgetAvailable()){score-=60;actions.add("补充预算或降低投资申请金额");}if(request.approvedAmount()==0){score-=40;actions.add("完成投资金额审批");}if(!request.procurementCompliant()){score-=50;actions.add("完成采购合规与合同检查");}if(request.overdueMilestones()>0){score-=Math.min(30,request.overdueMilestones()*5);actions.add("处理逾期里程碑并更新预测");}if(request.projectProgress()<100){score-=25;actions.add("完成剩余项目执行任务");}if(!request.acceptancePassed()){score-=45;actions.add("完成实物和业务验收");}if(!request.capitalizationReady()){score-=40;actions.add("补齐结算与资产转固资料");}double payback=request.expectedAnnualBenefit()==0?0:request.approvedAmount()/request.expectedAnnualBenefit();return result(score,actions,"READY_TO_CAPITALIZE","HOLD","BLOCKED",Map.of("budgetHeadroom",request.budgetAvailable()-request.requestedAmount(),"approvedAmount",request.approvedAmount(),"projectProgress",request.projectProgress(),"estimatedPaybackYears",Math.round(payback*10)/10d)); }
 private DecisionResult result(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=80?good:score>=50?warn:bad;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 private DecisionResult riskResult(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=70?bad:score>=40?warn:good;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 public record DecisionRequest(
        @NotBlank String requestNo,
        @Positive double requestedAmount,
        @PositiveOrZero double budgetAvailable,
        @PositiveOrZero double approvedAmount,
        @PositiveOrZero double expectedAnnualBenefit,
        @DecimalMin("0") @DecimalMax("100") double projectProgress,
        @PositiveOrZero int overdueMilestones,
        boolean procurementCompliant,
        boolean acceptancePassed,
        boolean capitalizationReady) {}
 public record DecisionResult(String decision,int score,Map<String,Object> metrics,List<String> actions) {}
}
