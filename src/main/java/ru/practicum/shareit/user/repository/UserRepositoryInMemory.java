package ru.practicum.shareit.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryInMemory implements UserRepository {

    private HashMap<Long, User> users;
    private Long maxId;

    @Autowired
    public UserRepositoryInMemory() {
        users = new HashMap<>();
        maxId = 0L;
    }

    public Long getNewId() {
        maxId++;
        return maxId;
    }

    @Override
    public Collection<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public User create(User user) {
        Long id = getNewId();
        user.setId(id);
        users.put(id, user);

        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        users.put(id, user);

        return user;
    }

    @Override
    public User delete(User user) {
        Long id = user.getId();
        users.remove(id);
        return user;
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public boolean isEmailExist(String Email, Long userId) {
        return users
                .values()
                .stream()
                .filter(user -> user.getId() != userId)
                .map(user -> user.getEmail())
                .filter(email -> Email.equals(email))
                .collect(Collectors.toList()).size() > 0;
    }

    @Override
    public boolean isUserExist(Long id) {
        return users.get(id) != null;
    }
}
