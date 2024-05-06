import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Создаем объект Scanner для чтения ввода от пользователя
        Scanner scanner = new Scanner(System.in);

        // Ввод значения
        System.out.print("Введите значение: ");

        // Читаем ввод от пользователя
        String input = scanner.nextLine();

        try {
            // Вызываем метод calc для вычисления результата
            String result = calc(input);
            // Выводим результат
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            // Выводим ошибку, если она возникла
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public static String calc(String input) throws Exception {
        // Разбиваем входную строку на токены (числа и оператор)
        String[] tokens = input.split(" ");

        // Проверяем, является ли входная строка корректным арифметическим выражением
        if (tokens.length < 3) {
            throw new Exception("Строка не является математической операцией");
        }

        if (tokens.length > 3) {
            throw new Exception(
                    "Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

        // Определяем, являются ли операнды римскими числами
        int num1, num2;
        boolean isRoman = false;

        try {
            // Преобразуем операнды в арабские числа
            num1 = Integer.parseInt(tokens[0]);
            num2 = Integer.parseInt(tokens[2]);
        } catch (NumberFormatException e) {
            // Если операнды не могут быть преобразованы в арабские числа, то они римские
            num1 = RomanNumeralConverter.romanToArabic(tokens[0]);
            num2 = RomanNumeralConverter.romanToArabic(tokens[2]);
            isRoman = true;
        }

        // Проверяем, находятся ли операнды в диапазоне 1-10
        if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
            throw new Exception("Неподходящие числа");
        }

        // Определяем оператор
        String operator = tokens[1];

        // Выполняем арифметическую операцию
        int result;
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                result = num1 / num2;
                break;
            default:
                throw new Exception("Неподходящая операция");
        }

        // Если операнды были римскими, то преобразуем результат в римское число
        if (isRoman) {
            if (result < 1) {
                throw new Exception("В римской системе нет отрицательных чисел");
            }
            return RomanNumeralConverter.arabicToRoman(result);
        } else {
            // В противном случае возвращаем результат как строку
            return String.valueOf(result);
        }
    }

    static class RomanNumeralConverter {
        // Массив римских символов
        private static final String[] ROMAN_SYMBOLS = { "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C" };

        // Метод для преобразования римского числа в арабское
        public static int romanToArabic(String roman) throws Exception {
            int result = 0;
            int i = 0;

            while (i < roman.length()) {
                String currentSymbol = Character.toString(roman.charAt(i));

                if (i < roman.length() - 1) {
                    String nextSymbol = Character.toString(roman.charAt(i + 1));
                    String combinedSymbol = currentSymbol + nextSymbol;

                    if (isRomanSymbol(combinedSymbol)) {
                        result += getArabicValue(combinedSymbol);
                        i += 2;
                        continue;
                    }
                }

                if (isRomanSymbol(currentSymbol)) {
                    result += getArabicValue(currentSymbol);
                    i++;
                } else {
                    throw new Exception("Используются одновременно разные системы счисления");
                }
            }

            return result;
        }

        // Метод для преобразования арабского числа в римское
        public static String arabicToRoman(int arabic) {
            StringBuilder result = new StringBuilder();

            for (int i = ROMAN_SYMBOLS.length - 1; i >= 0; i--) {
                while (arabic >= getArabicValue(ROMAN_SYMBOLS[i])) {
                    result.append(ROMAN_SYMBOLS[i]);
                    arabic -= getArabicValue(ROMAN_SYMBOLS[i]);
                }
            }

            return result.toString();
        }

        // Метод для проверки, является ли символ римским
        private static boolean isRomanSymbol(String symbol) {
            for (String romanSymbol : ROMAN_SYMBOLS) {
                if (romanSymbol.equals(symbol)) {
                    return true;
                }
            }
            return false;
        }

        // Метод для получения арабского значения римского символа
        private static int getArabicValue(String symbol) {
            switch (symbol) {
                case "I":
                    return 1;
                case "IV":
                    return 4;
                case "V":
                    return 5;
                case "IX":
                    return 9;
                case "X":
                    return 10;
                case "XL":
                    return 40;
                case "L":
                    return 50;
                case "XC":
                    return 90;
                case "C":
                    return 100;
                default:
                    return 0;
            }
        }
    }
}