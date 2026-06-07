package ai.ghosty.paycheck.util;

import ai.ghosty.paycheck.model.Policy;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;

public class ConfManager {
    private final static Path JSON_FILE = Path.of("../Policy.json");

    public static void createPolicyConf(Policy policy) {
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
            }
            catch (Exception e) {
                System.err.println("[error] failed to write to configuration file");
            }
        }
        catch (IOException e) {
            System.err.println("[error] failed to save policy configuration");
        }
    }

    public static Policy readPolicyConf() {
        BigDecimal[] values = new BigDecimal[11];

        try (BufferedReader reader = new BufferedReader(new FileReader(JSON_FILE.toFile()))) {
            String line, value;
            byte i = 0;

            while ((line = reader.readLine()) != null) {
                if (line.split(":").length == 1) {
                    values[i] = null;
                    i++;
                    continue;
                }

                if (line.contains(":")) {
                    values[i] = (!line.split(":")[1].trim().isEmpty())
                            ? new BigDecimal(line.split(":")[1].trim()) : null;

                    i++;
                }
            }
        }
        catch (IOException e) {
            System.err.println("[error] failed to read policy configuration");
        }

        return new Policy(values[0], values[1], values[2], values[3],values[4],values[5],values[6],
                values[7],values[8],values[9],values[10]);
    }
}
