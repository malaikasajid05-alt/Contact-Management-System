package com.malaika.backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="Email")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name= "email", nullable = false)
    private String email;

    @Column(name= "label")
    private String eLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="contact_id")
    private  Contact contact;
}
