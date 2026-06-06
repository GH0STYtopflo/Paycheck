package ai.ghosty.paycheck.util;

import ai.ghosty.paycheck.service.UserServices;

import java.util.Random;

public class IDGen {
    public static int generateUniqueID() {
        Random rand = new Random();
        int id = rand.nextInt(1000,10000);

        if (UserServices.idExists(id)) id = generateUniqueID();

        return id;
    }
}
