package com.efd.model;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.List;

/**
 * Created by volodymyr on 13.06.17.
 */
@Entity
@Table(name = "COUNTRY")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "id")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getJSON() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        return object;
    }
}
