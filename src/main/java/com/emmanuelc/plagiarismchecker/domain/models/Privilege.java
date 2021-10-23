package com.emmanuelc.plagiarismchecker.domain.models;

import com.emmanuelc.plagiarismchecker.domain.models.enumerations.PrivilegeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@Accessors(chain = true)
@Entity
public class Privilege implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PrivilegeEnum name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;
}
