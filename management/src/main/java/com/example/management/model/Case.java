package com.example.management.model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="cases")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    @Transient
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "creationDate")
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @PrePersist
    protected void onCreate(){
        this.creationDate = new Date();
        this.status = Status.NEW;
        this.uuid = UUID.randomUUID();
    }

    public Case(){}

    public Case(String title, String description, String severity, String status){
        this.title = title;
        this.description = description;
        this.severity = searchEnum(Severity.class, severity);// Severity.valueOf(severity);
        this.status = searchEnum(Status.class, status);
    }

    public Case(String title, String description, Severity severity){
        this.title = title;
        this.description = description;
        this.severity = severity;
    }

    public Case(String title, String description, Severity severity, User user){
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.user = user;
    }
    private static <T extends Enum<?>> T searchEnum(Class<T> enumeration,
                                                   String search) {
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return each;
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }
    public UUID getUuid(){return uuid;}
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public Severity getSeverity() {
        return severity;
    }
    public Status getStatus() {
        return status;
    }
    public User getUser(){return user;}


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
    public void setSeverity(String severity){this.severity = searchEnum(Severity.class, severity);}


    public void setStatus(Status status) {
        this.status = status;
    }
    public void setStatus(String status){this.status = searchEnum(Status.class, status);}

    public void setUser(User user){this.user = user;}
}
