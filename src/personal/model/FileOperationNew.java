package personal.model;

import java.util.ArrayList;
import java.util.List;

public class FileOperationNew implements Repository {
    private NewUserMapper mapper = new NewUserMapper();
    private FileOperation fileOperation;

    public FileOperationNew(FileOperation fileOperation) {
        this.fileOperation = fileOperation;

    }

    @Override
    public List<User> getAllUsers() {
        List<String> lines = fileOperation.readAllLines();
        lines.removeIf(p -> p.equals(""));
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.map(line));
        }
        return users;
    }

    @Override
    public String CreateUser(User user) {
        List<User> users = getAllUsers();
        int max = 0;
        for (User item : users) {
            int id = Integer.parseInt(item.getId());
            if (max < id) {
                max = id;
            }
        }
        int newId = max + 1;
        String id = String.format("%d", newId);
        user.setId(id);
        users.add(user);
        writeToFile(users);
        return id;
    }

    @Override
    public void updateUser(User updateUser) {
        List<User> users = getAllUsers();
        User userToBeUpdated = users.stream().filter(p -> p.getId().equals(updateUser.getId())).findFirst().orElse(null);
        if (userToBeUpdated != null) {
            userToBeUpdated.setFirstName(updateUser.getFirstName());
            userToBeUpdated.setLastName(updateUser.getLastName());
            userToBeUpdated.setPhone(updateUser.getPhone());
            writeToFile(users);
        }
        else System.out.println("User not found");

    }

    @Override
    public void deleteUser(User user) {
        List<User> users = getAllUsers();
        users.removeIf(p -> p.getId().equals(user.getId()));
        writeToFile(users);
    }
    private void writeToFile(List<User> users){
        List<String> lines = new ArrayList<>();
        for (User item : users) {
            lines.add(mapper.map(item));
        }
        fileOperation.saveAllLines(lines);
    }
}
