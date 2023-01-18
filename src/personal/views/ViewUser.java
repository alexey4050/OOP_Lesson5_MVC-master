package personal.views;

import personal.controllers.UserController;
import personal.model.*;

import java.util.List;
import java.util.Scanner;

public class ViewUser {

    private UserController userController;

    public ViewUser(UserController userController) {
        this.userController = userController;
    }

    public void run(){
        Commands com = Commands.NONE;
        FileOperation fileOperation;
        Repository repository;

        String mode = prompt("Введите режим сохранения файла (1. текстовый(,) 2. текстовый(;)");
        while (!mode.equals("1") && (!mode.equals("2"))){
            mode = prompt("Введите режим сохранения файла (1. текстовый (,) 2.текстовый (;)");
        }
        switch (mode){
            case "1":
                System.out.println("Выбрали режим сохранения в текстовый файл (через запятую)");
                fileOperation = new FileOperationImpl("users.txt");
                repository = new RepositoryFile(fileOperation);
                this.userController = new UserController(repository);
                break;
            case "2":
                System.out.println("Выбрали режим сохранения в текстовый файл (через точку с запятой и строку разделитель)");
                fileOperation = new FileOperationImpl("users2.txt");
                repository = new FileOperationNew(fileOperation);
                this.userController = new UserController(repository);
                break;

        }

        while (true) {
            String command = prompt("Введите команду: ");
            com = Commands.valueOf(command.toUpperCase());
            if (com == Commands.EXIT) return;
            try{
            switch (com) {
                case CREATE:
                    String firstName = prompt("Имя: ");
                    String lastName = prompt("Фамилия: ");
                    String phone = prompt("Номер телефона: ");
                    userController.saveUser(new User(firstName, lastName, phone));
                    break;
                case READ:
                    String id = prompt("Идентификатор пользователя: ");
                    try {
                        User user = userController.readUser(id);
                        System.out.println(user);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case LIST:
                    List<User> lst = userController.readList();
                    lst.forEach(i ->System.out.println(i + "\n"));
                    break;
                case UPDATE:
                    String numId = prompt("Какой контакт редактировать? Введите номер ID: ");
                    userController.idPresenceValidation(numId);
                    userController.updateUser(numId, createAGuy());
                    break;
                case DELETE:
                    String delId = prompt("Укажите какой id удалить?");
                    userController.deleteUser(delId);
                    break;
            }
        }catch (Exception e){
                System.out.println("Oopsie!\n" + e.getMessage());
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }
    private User createAGuy(){
        String firstName = prompt("Имя: ");
        String lastName = prompt("Фамилия: ");
        String phone = prompt("Номер телефона: ");
        User newGuy = new User(firstName, lastName,phone);
        return newGuy;
    }
}
