package nguyenduchai.cse.com.model;

import java.io.Serializable;

/**
 * Created by Nguyen Duc Hai on 11/10/2015.
 */
public class Identifier implements Serializable {
    private long id;
    private String name;

    public Identifier(){}

    public long getId() {
        return id;
    }

    public Identifier setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Identifier setName(String name) {
        this.name = name;
        return this;
    }
}
