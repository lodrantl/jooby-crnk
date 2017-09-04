package si.lodrant.jooby.crnk.app;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Beer {
    @Id
    @GeneratedValue
    public Long id;

    public String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}