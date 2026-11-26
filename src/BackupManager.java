import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class BackupManager {
    private final Path backupDir = Paths.get("backups");

    // Створення ZIP-архіву із збережених файлів
    public void createZipBackup(String... filenames) {
        try {
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path zipPath = backupDir.resolve("backup_" + timestamp + ".zip");

            int addedFiles = 0;
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                for (String name : filenames) {
                    Path file = Paths.get(name);
                    if (Files.exists(file)) {
                        zos.putNextEntry(new ZipEntry(file.getFileName().toString()));
                        Files.copy(file, zos);
                        zos.closeEntry();
                        addedFiles++;
                    }
                }
            }

            if (addedFiles == 0) {
                Files.deleteIfExists(zipPath);
                System.out.println("Немає збережених файлів для архівації. Спочатку збережіть дані.");
            } else {
                long size = Files.size(zipPath);
                System.out.println("Резервну копію створено: " + zipPath + " (" + size + " байт)");
            }
        } catch (IOException e) {
            System.out.println("Помилка створення ZIP-архіву: " + e.getMessage());
        }
    }

    // Відновлення файлів з останнього ZIP-архіву
    public boolean restoreFromLatestBackup() {
        try {
            if (!Files.exists(backupDir)) {
                System.out.println("Папка резервних копій не існує.");
                return false;
            }

            List<Path> backups = getSortedBackups();
            if (backups.isEmpty()) {
                System.out.println("У папці backups немає жодного ZIP-архіву.");
                return false;
            }

            Path latestZip = backups.get(backups.size() - 1);
            try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(latestZip))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path targetFile = Paths.get(entry.getName()).getFileName();
                    Files.copy(zis, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Відновлено файл: " + targetFile);
                    zis.closeEntry();
                }
            }

            System.out.println("Дані успішно відновлено з архіву: " + latestZip.getFileName());
            return true;
        } catch (IOException e) {
            System.out.println("Помилка відновлення з ZIP-архіву: " + e.getMessage());
            return false;
        }
    }

    // NIO.2: видалення старих бекапів (залишає тільки найновіший)
    public void cleanOldBackups() {
        try {
            if (!Files.exists(backupDir)) {
                System.out.println("Папка backups ще не створена.");
                return;
            }

            List<Path> backups = getSortedBackups();
            int deleted = 0;
            for (int i = 0; i < backups.size() - 1; i++) {
                Files.deleteIfExists(backups.get(i));
                deleted++;
            }
            System.out.println("Видалено застарілих бекапів: " + deleted);
        } catch (IOException e) {
            System.out.println("Помилка очищення бекапів: " + e.getMessage());
        }
    }

    private List<Path> getSortedBackups() throws IOException {
        try (Stream<Path> stream = Files.list(backupDir)) {
            return stream
                    .filter(p -> p.toString().endsWith(".zip"))
                    .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                    .collect(Collectors.toList());
        }
    }
}