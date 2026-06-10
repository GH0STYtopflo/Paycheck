package ai.ghosty.paycheck.util;

import ai.ghosty.paycheck.model.Tables;

import java.util.Random;

public class IDGen {

    public static int generateUniqueID(Tables table) {
        Random rand = new Random();
        int id = rand.nextInt(table.idRange[0],table.idRange[1]);

        if (table.getTableIDs().contains(id)) id = generateUniqueID(table);
        return id;
    }
}
