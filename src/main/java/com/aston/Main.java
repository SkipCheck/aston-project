package com.aston;

import com.aston.entity.User;
import com.aston.exception.UserException;
import com.aston.service.UserService;
import com.aston.utils.HibernateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();


    public static void main(String[] args) {
        log.info("Запуск сервиса контроля пользователей приложения");

        try {
            displayMenu();

            boolean running = true;
            while (running) {
                log.info("\nВыберите действие: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        createUser();
                        break;
                    case "2":
                        getUserById();
                        break;
                    case "3":
                        getAllUsers();
                        break;
                    case "4":
                        getUsersByName();
                        break;
                    case "5":
                        updateUser();
                        break;
                    case "6":
                        deleteUser();
                        break;
                    case "7":
                        getUserByEmail();
                        break;
                    case "0":
                        running = false;
                        log.info("Выход из приложения...");
                        break;
                    default:
                        log.info("Неверный выбор. Попробуйте снова.");
                }

                if (running) {
                    displayMenu();
                }
            }

        } catch (Exception e) {
            log.error("Произошла критическая ошибка: {}", e.getMessage(), e);
        } finally {
            scanner.close();
            HibernateUtil.shutdown();
            log.info("Приложение завершено");
        }
    }

    private static void displayMenu() {
        log.info("\nСписок команд:");
        log.info("1. Создать пользователя");
        log.info("2. Найти пользователя по ID");
        log.info("3. Получить всех пользователей");
        log.info("4. Найти пользователей по имени");
        log.info("5. Обновить пользователя");
        log.info("6. Удалить пользователя");
        log.info("7. Найти пользователя по email");
        log.info("0. Выход");
    }

    private static void createUser() {
        try {
            log.info("\nСоздание нового пользователя ");

            log.info("Введите имя: ");
            String name = scanner.nextLine();

            log.info("Введите email: ");
            String email = scanner.nextLine();

            log.info("Введите возраст (или оставьте пустым): ");
            String ageInput = scanner.nextLine();
            Integer age = ageInput.isEmpty() ? null : Integer.parseInt(ageInput);

            User user = userService.createUser(name, email, age);

            log.info("Пользователь успешно создан!");
            log.info("ID: " + user.getId());
            log.info("Имя: " + user.getName());
            log.info("Email: " + user.getEmail());
            if (user.getAge() != null) {
                log.info("Возраст: " + user.getAge());
            }

            log.info("Дата создания: " + user.getCreatedAt());

        } catch (NumberFormatException e) {
            log.info("Ошибка: возраст должен быть числом");
        } catch (UserException e) {
            log.info("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            log.info("Произошла ошибка при создании пользователя: " + e.getMessage());
        }
    }

    private static void getUserById() {
        try {
            log.info("\nПоиск пользователя по ID");
            
            log.info("Введите ID пользователя: ");
            Long id = Long.parseLong(scanner.nextLine());

            Optional<User> user = userService.getUserById(id);

            if (user.isPresent()) {
                displayUser(user.get());
            } else {
                log.info("Пользователь с ID " + id + " не найден");
            }

        } catch (NumberFormatException e) {
            log.info("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            log.info("Произошла ошибка при поиске пользователя: " + e.getMessage());
        }
    }

    private static void getAllUsers() {
        try {
            log.info("\nСписок всех пользователей ");

            List<User> users = userService.getAllUsers();

            if (users.isEmpty()) {
                log.info("Пользователи не найдены");
            } else {
                log.info("Найдено пользователей: " + users.size());
                for (User user : users) {
                    displayUserShort(user);
                }
            }

        } catch (Exception e) {
            log.info("Произошла ошибка при получении пользователей: " + e.getMessage());
        }
    }

    private static void getUsersByName() {
        try {
            log.info("\nПоиск пользователей по имени");

            log.info("Введите имя (или часть имени): ");
            String name = scanner.nextLine();

            List<User> users = userService.getUsersByName(name);

            if (users.isEmpty()) {
                log.info("Пользователи не найдены");
            } else {
                log.info("Найдено пользователей: " + users.size());
                for (User user : users) {
                    displayUserShort(user);
                }
            }

        } catch (Exception e) {
            log.info("Произошла ошибка при поиске пользователей: " + e.getMessage());
        }
    }

    private static void updateUser() {
        try {
            log.info("\nОбновление пользователя ");

            log.info("Введите ID пользователя для обновления: ");
            Long id = Long.parseLong(scanner.nextLine());

            log.info("Введите новое имя: ");
            String name = scanner.nextLine();

            log.info("Введите новый email: ");
            String email = scanner.nextLine();

            log.info("Введите новый возраст (или оставьте пустым): ");
            String ageInput = scanner.nextLine();
            Integer age = ageInput.isEmpty() ? null : Integer.parseInt(ageInput);

            userService.updateUser(id, name, email, age);

            log.info("Пользователь успешно обновлен!");

        } catch (NumberFormatException e) {
            log.info("Ошибка: ID и возраст должны быть числами");
        } catch (UserException e) {
            log.info("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            log.info("Произошла ошибка при обновлении пользователя: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try {
            log.info("\nУдаление пользователя ");

            log.info("Введите ID пользователя для удаления: ");
            Long id = Long.parseLong(scanner.nextLine());

            log.info("Вы уверены, что хотите удалить пользователя? (y/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("y")) {
                userService.deleteUser(id);
                log.info("Пользователь успешно удален");
            } else {
                log.info("Удаление отменено");
            }

        } catch (NumberFormatException e) {
            log.info("Ошибка: ID должен быть числом");
        } catch (UserException e) {
            log.info("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            log.info("Произошла ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    private static void getUserByEmail() {
        try {
            log.info("\nПоиск пользователя по email ");

            log.info("Введите email пользователя: ");
            String email = scanner.nextLine();

            Optional<User> user = userService.getUserByEmail(email);

            if (user.isPresent()) {
                displayUser(user.get());
            } else {
                log.info("Пользователь с email " + email + " не найден");
            }

        } catch (Exception e) {
            log.info("Произошла ошибка при поиске пользователя: " + e.getMessage());
        }
    }

    private static void displayUser(User user) {
        log.info("\nИнформация о пользователе");
        log.info("ID: " + user.getId());
        log.info("Имя: " + user.getName());
        log.info("Email: " + user.getEmail());
        if (user.getAge() != null) {
            log.info("Возраст: " + user.getAge());
        }
        log.info("Дата создания: " + user.getCreatedAt());
    }

    private static void displayUserShort(User user) {
        log.info("ID: {} | Имя: {} | Email: {} | Возраст: {} | Дата создания: {}",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge() != null ? user.getAge() : "не указан",
                user.getCreatedAt());
    }

}
