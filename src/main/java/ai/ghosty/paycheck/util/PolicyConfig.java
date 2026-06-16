package ai.ghosty.paycheck.util;

import ai.ghosty.paycheck.logger.LogLevel;
import ai.ghosty.paycheck.logger.Logger;
import ai.ghosty.paycheck.model.Policy;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;


/* I specifically wrote this class instead of using a library like jackson or gson just to demonstrate that
   we're not incapable of handling file io. it's just lame and time-consuming.
*/
public class PolicyConfig {
    private final static Path JSON_FILE = Path.of("Policy.json");

    public static void updatePolicyConf(Policy policy) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JSON_FILE.toFile()))) {
            String text = String.format("{\n" +
                    "  \"INCOME_TAX_RATE\": %s,\n" +
                    "  \"SOCIAL_SECURITY_RATE\": %s,\n" +
                    "  \"HEALTHCARE_RATE\": %s,\n" +
                    "  \"INSURANCE_RATE\": %s,\n" +
                    "  \"OVERTIME_MULTIPLIER\": %s,\n" +
                    "  \"MAX_LOAN_REPAY_RATE\": %s,\n" +
                    "  \"ACCOMMODATION_FLAT_RATE\": %s,\n" +
                    "  \"MEAL_ALLOWANCE_FLAT_RATE\": %s,\n" +
                    "  \"RECREATION_PER_FAMILY_MEMBER\": %s,\n" +
                    "  \"CHILD_ALLOWANCE_PER_CHILD\": %s,\n" +
                    "  \"WOMEN_EXTRA\": %s\n" +
                    "}  \n", policy.getINCOME_TAX_RATE().toString(), policy.getSOCIAL_SECURITY_RATE().toString(),
                    policy.getHEALTHCARE_RATE().toString(), policy.getINSURANCE_RATE().toString(),
                    policy.getOVERTIME_MULTIPLIER().toString(), policy.getMAX_LOAN_REPAY_RATE().toString(),
                    policy.getACCOMMODATION_FLAT_RATE().toString(), policy.getMEAL_ALLOWANCE_FLAT_RATE().toString(),
                    policy.getRECREATION_PER_FAMILY_MEMBER().toString(), policy.getCHILD_ALLOWANCE_PER_CHILD().toString(),
                    policy.getWOMEN_EXTRA().toString());

            try {
                writer.write(text);
                Logger.log("saved policy config successfully", LogLevel.INFO);
            }
            catch (Exception e) {
                Logger.log("failed to save write config: " + e.getMessage(), LogLevel.WARN);
            }
        }
        catch (IOException e) {
            Logger.log("failed to save policy config: " + e.getMessage(), LogLevel.WARN);
        }
    }

    public static Policy readPolicyConf() {
        BigDecimal[] fields = new BigDecimal[11];

        try (BufferedReader reader = new BufferedReader(new FileReader(JSON_FILE.toFile()))) {
            String line, value;
            byte i = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(":")) continue;

                value = line.split(":")[1].trim();
                if (value.contains(",")) value = value.split(",")[0];

                fields[i++] = new BigDecimal(value);
            }

            return new Policy(fields[0], fields[1], fields[2], fields[3], fields[4],
                    fields[5], fields[6], fields[7], fields[8], fields[9], fields[10]);
        }
        catch (IOException e) {
            Logger.log("failed to read policy config returned default policy: " + e.getMessage(), LogLevel.WARN);
        }

        return new Policy();
    }
}
