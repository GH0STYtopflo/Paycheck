package ai.ghosty.paycheck.util;

import ai.ghosty.paycheck.service.UserServices;

import java.util.Random;

public class IDGen {
    //TODO UUID Generation must be done relatively to the table

    public static int generateUniqueID() {
        Random rand = new Random();
        int id = rand.nextInt(1000,10000);

        return id;
    }
}
