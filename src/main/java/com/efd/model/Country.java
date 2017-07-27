package com.efd.model;

import com.efd.core.Constants;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by volodymyr on 13.06.17.
 */
@Entity
@Table(name = "COUNTRY")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;


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

    public JSONObject getJSON() throws Exception {
        JSONObject object = new JSONObject();
        object.put(Constants.KEY_ID, id);
        object.put(Constants.KEY_NAME, name);
        return object;
    }
}
