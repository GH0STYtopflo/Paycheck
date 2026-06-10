import ai.ghosty.paycheck.model.Policy;
import ai.ghosty.paycheck.util.PolicyConfig;

public class Test {
    public static void main(String[] args) {
        Policy policy = PolicyConfig.readPolicyConf();
        System.out.println(policy.toString());
    }
}
