/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.capex.domain;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class DomainCatalog {
    private final Map<String, WorkflowAction> actions = new LinkedHashMap<>();
    public DomainCatalog() {
        actions.put("SUBMIT", new WorkflowAction("SUBMIT", "提交投资审批", List.of("草稿"), "待审批", "OPERATOR"));
        actions.put("APPROVE", new WorkflowAction("APPROVE", "批准投资项目", List.of("待审批"), "执行中", "ADMIN"));
        actions.put("CAPITALIZE", new WorkflowAction("CAPITALIZE", "验收并转固", List.of("执行中"), "已转固", "ADMIN"));
    }
    public String systemName() { return "知华科技企业资本性支出管理系统"; }
    public String scene() { return "投资规划、项目立项、预算占用、审批、采购、执行、变更、验收、转固和收益跟踪"; }
    public String initialStatus() { return "草稿"; }
    public String partyLabel() { return "投资项目/责任中心"; }
    public String amountLabel() { return "投资金额"; }
    public String quantityLabel() { return "投资项目数"; }
    public String dueLabel() { return "投产期限"; }
    public List<ModuleDefinition> modules() { return List.of(
            new ModuleDefinition("PORTFOLIO", "投资规划", "维护年度投资方向、额度、类别和项目组合"),
            new ModuleDefinition("REQUEST", "项目立项", "登记商业理由、范围、里程碑、收益和责任人"),
            new ModuleDefinition("BUDGET", "预算控制", "执行预算申请、占用、释放、调剂和超支拦截"),
            new ModuleDefinition("APPROVAL", "投资审批", "按金额、组织、类别和风险执行分级决策"),
            new ModuleDefinition("PROCUREMENT", "采购关联", "关联采购申请、合同、订单、付款和供应商"),
            new ModuleDefinition("EXECUTION", "项目执行", "跟踪进度、成本、承诺金额、现金流和风险"),
            new ModuleDefinition("CHANGE", "投资变更", "管理追加、范围、工期、收益变更和重新审批"),
            new ModuleDefinition("ACCEPTANCE", "验收与转固", "完成实物验收、结算、资产拆分和会计转固"),
            new ModuleDefinition("BENEFIT", "投后评价", "对比计划与实际收益、产能、节省和回收期")
        ); }
    public Map<String, WorkflowAction> actions() { return Collections.unmodifiableMap(actions); }
    public record ModuleDefinition(String code,String name,String description) {}
    public record WorkflowAction(String code,String label,List<String> from,String to,String requiredRole) {}
}
