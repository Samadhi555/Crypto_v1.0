import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final List<Character> arrays = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    private static final String TEXT = "Введите путь к файлу, текст которого нужно ";

    private static int c;
    private static int x;
    private static boolean b;

    public static int varMode = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите режим:");
        System.out.println("1 - Зашифровать текст с помощью ключа");
        System.out.println("2 - Расшифровать текст с помощью ключа");
        System.out.println("3 - Криптоанализ методом 'Brute Force'");
        int mode = scanner.nextInt();
        scanner.nextLine();

        if (mode == 1) {
            System.out.println(TEXT + "зашифровать:");
            String inputFile = scanner.nextLine();
            //String inputFile = "Test.txt";
            System.out.println("Введите ключ:");
            int key = scanner.nextInt();
            encryption(inputFile, key);
        } else if (mode == 2) {
            varMode = 2;
            System.out.println(TEXT + "расшифровать:");
            String inputFile = scanner.nextLine();
            System.out.println("Введите ключ:");
            int key = scanner.nextInt();
            decryption(inputFile, key);
        } else if (mode == 3) {
            System.out.println(TEXT + "расшифровать:");
            String inputFile = scanner.nextLine();
            // String inputFile = "encrypted.txt";
            bruteForce(inputFile);
        } else {
            System.out.println("Такого режима нет!");
        }
        scanner.close();
    }

    public static void encryption(String inputFile, int key) {

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter("encrypted.txt", StandardCharsets.UTF_8))) {
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                if (Character.isUpperCase(character)) {
                    character = Character.toLowerCase(character);
                    b = true;
                } else b = false;
                if (arrays.contains(character)) {
                    x = arrays.indexOf(character);
                    x = (x + key) % arrays.size();
                    character = arrays.get(x);
                    if (b) {
                        character = Character.toUpperCase(character);
                    }
                }
                writer.write(character);
            }
            System.out.println("Текст зашифрован в файл 'encrypted.txt'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decryption(String inputFile, int key) {

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile, StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter("decrypted.txt"))) {
            while ((c = reader.read()) != -1) {
                char character = (char) c;
                if (Character.isUpperCase(character)) {
                    character = Character.toLowerCase(character);
                    b = true;
                } else b = false;
                if (arrays.contains(character)) {
                    x = arrays.indexOf(character);
                    if (x < key) {
                        x = (arrays.size() + x) - (key % arrays.size());
                    } else {
                        x = (x - key) % arrays.size();
                    }
                    character = arrays.get(x);
                    if (b) {
                        character = Character.toUpperCase(character);
                    }
                }
                writer.write(character);
            }
            if (varMode == 2) System.out.println("Текст расшифрован в файл 'decrypted.txt'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bruteForce(String inputFile) {

        for (int i = 1; i < arrays.size(); i++) {
            decryption(inputFile, i);
            int count = counterWhiteSpace();
            if (count <= 12) {
                if (isDecrypt("decrypted.txt", StandardCharsets.UTF_8)) {
                    System.out.println("Текст расшифрован в файл 'decrypted.txt', ключ=" + i + ".");
                    break;
                }
            }
        }
    }

    private static int counterWhiteSpace() {

        try {
            String str = readFile("decrypted.txt", StandardCharsets.UTF_8);
            int input = str.length();
            int output = str.replaceAll(" ", "").length();
            int whiteSpace = input - output;
            int res = 13;
            if (whiteSpace > 0) {
                res = input / whiteSpace;
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        return Files.readString(Paths.get(path), encoding);
    }

    public static boolean isDecrypt(String path, Charset encoding) {

        String str;

        try {
            str = Files.readString(Paths.get(path), encoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] arrStr = str.split(" ");

        for (String s : arrStr) {
            if (s.length() > 3) {
                for (int j = 1; j < s.length() - 1; j++) {
                    if (s.charAt(j) == ',' || s.charAt(j) == ':' || s.charAt(j) == '?' && s.charAt(j) == '.') {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
