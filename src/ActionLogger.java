import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;




    public class ActionLogger {
        private static final int MAX_LINES = 5; // Ліміт рядків на один файл для наочної ротації
        private final Path logDir = Paths.get("logs");
        private int currentFileIndex = 1;
        private int currentLineCount = 0;

        public ActionLogger() {
            initLogDirectory();
        }

        // NIO.2: перевірка існування директорії та її створення за потреби
        private void initLogDirectory() {
            try {
                if (!Files.exists(logDir)) {
                    Files.createDirectories(logDir);
                }

                while (Files.exists(getLogFilePath(currentFileIndex))) {
                    long lines = Files.readAllLines(getLogFilePath(currentFileIndex), StandardCharsets.UTF_8).size();
                    if (lines < MAX_LINES) {
                        currentLineCount = (int) lines;
                        break;
                    }
                    currentFileIndex++;
                }
            } catch (IOException e) {
                System.out.println("Помилка ініціалізації папки логів: " + e.getMessage());
            }
        }

        private Path getLogFilePath(int index) {
            return logDir.resolve("app_log_" + index + ".txt");
        }

        // Запис дії через буферизований потік (BufferedWriter) + try-with-resources
        public void log(String action) {
            if (currentLineCount >= MAX_LINES) {
                currentFileIndex++;
                currentLineCount = 0;
            }

            Path currentFile = getLogFilePath(currentFileIndex);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String entry = "[" + time + "] " + action;

            try (BufferedWriter writer = Files.newBufferedWriter(
                    currentFile,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                writer.write(entry);
                writer.newLine();
                currentLineCount++;
            } catch (IOException e) {
                System.out.println("Не вдалося записати лог: " + e.getMessage());
            }
        }


        public void cleanOldLogs() {
            try {
                System.out.println("\n--- Стан файлів логів (NIO.2) ---");
                int deletedCount = 0;
                for (int i = 1; i <= currentFileIndex; i++) {
                    Path file = getLogFilePath(i);
                    if (Files.exists(file)) {
                        long size = Files.size(file);
                        System.out.println("Файл: " + file.getFileName() + " | Розмір: " + size + " байт");

                        // Видаляємо всі файли, крім поточного активного
                        if (i < currentFileIndex) {
                            Files.deleteIfExists(file);
                            deletedCount++;
                        }
                    }
                }
                System.out.println("Видалено застарілих файлів логів: " + deletedCount);
            } catch (IOException e) {
                System.out.println("Помилка при роботі з файлами логів: " + e.getMessage());
            }
        }
    }


