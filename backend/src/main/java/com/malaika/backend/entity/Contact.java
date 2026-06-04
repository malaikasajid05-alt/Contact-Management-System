package com.malaika.backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name ="Contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name= "first_name", nullable = false)
    private String firstName;

    @Column(name= "last_name")
    private String lastName;

    @Column(name= "title")
    private String title;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneNo> phoneNos;


}
