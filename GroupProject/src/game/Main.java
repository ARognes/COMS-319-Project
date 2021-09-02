package game;

import engine.*;

public class Main {


    public static void main(String[] args) {
        try {

            new Engine().run();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}