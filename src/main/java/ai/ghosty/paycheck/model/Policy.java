package ai.ghosty.paycheck.model;

import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;

public class Policy {
    public HashMap<String, BigDecimal> conf = new HashMap<>(5);

    public Policy(String overtime, String accommodation, String meal, String entertainment,
                  String children) {

        conf.put("ot", BigDecimal.valueOf(Integer.parseInt(overtime)));
        conf.put("accommodation", BigDecimal.valueOf(Integer.parseInt(accommodation)));
        conf.put("meal", BigDecimal.valueOf(Integer.parseInt(meal)));
        conf.put("entertainment", BigDecimal.valueOf(Integer.parseInt(entertainment)));
        conf.put("children", BigDecimal.valueOf(Integer.parseInt(children)));
    }

}
