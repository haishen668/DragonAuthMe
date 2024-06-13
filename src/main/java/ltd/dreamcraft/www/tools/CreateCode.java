package ltd.dreamcraft.www.tools;

import ltd.dreamcraft.www.Manager.ConfigManager;

import java.util.Random;

public class CreateCode {
    public static String getCode() {
        Random rand = new Random();
        char[] letters;
        if (ConfigManager.getSettingCodeMode() == 1) {
            letters = new char[]{
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
                    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                    'y', 'z', 'r', '0', '1', '2', '3', '4', '5', '6',
                    '7', '8', '9'};
        } else {
            letters = new char[]{
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        }

        String str = "";
        boolean[] flags = new boolean[letters.length];
        for (int i = 0; i < ConfigManager.getSettingCodeLength(); i++) {
            int index;
            do {
                index = rand.nextInt(letters.length);
            } while (flags[index]);
            char c = letters[index];
            str = str + c;
            flags[index] = true;
        }
        return str;
    }
}
