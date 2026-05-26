package com.malaika.backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="PhoneNo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name= "pNumber", nullable = false)
    private String pNum;

    @Column(name= "pLabel")
    private String pLabel;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name="contact_id")
    private  Contact contact;
}
