package si.lodrant.jooby.crnk;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nest_projects")
@NamedQueries({@NamedQuery(name = "Project.getByName", query = "select e from Project e where e.name = :projectName"),
        @NamedQuery(name = "Project.getAll", query = "select e from Project e")})
public class Project {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TS")
    @UpdateTimestamp
    private Date updateDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_TS")
    @CreationTimestamp
    private Date createDate;

    private String description;

    private Long defaultImageId;

    private String path;

    private String rolesAllowed;

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDefaultImageId() {
        return defaultImageId;
    }

    public void setDefaultImageId(Long defaultImageId) {
        this.defaultImageId = defaultImageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRolesAllowed() {
        return rolesAllowed;
    }

    public void setRolesAllowed(String rolesAllowed) {
        this.rolesAllowed = rolesAllowed;
    }
}
