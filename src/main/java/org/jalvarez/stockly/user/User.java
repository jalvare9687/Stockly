package org.jalvarez.stockly.user;

import jakarta.persistence.*;
import lombok.Data;
import org.jalvarez.stockly.location.model.Location;

@Entity
@Data
@Table(name = "users")
public class User { //create user when we finally implement auth
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;


}
