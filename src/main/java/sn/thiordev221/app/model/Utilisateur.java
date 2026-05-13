package sn.thiordev221.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name="utilisateurs",
    uniqueConstraints={
        @UniqueConstraint(columnNames = "email")
    }
)
public class Utilisateur{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false, unique=true, length=100)
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false)
    private String pseudo;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(
        name = "users_roles",
        joinColumns= @JoinColumn(name="user_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Column(nullable=false)
    private boolean actif;

    @OneToMany(mappedBy="proprietaire", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @Builder.Default
    private List<TodoList> todoLists = new ArrayList<>();

    @OneToMany(mappedBy="invite", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @Builder.Default
    private List<Partage> partages = new ArrayList<>();

    public Utilisateur(String email, String password){
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>(Collections.singletonList(Role.ROLE_USER));
        this.actif = true;
    }

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     if(roles == null || roles.isEmpty()) return Collections.emptyList();
    //     return this.roles.stream()
    //                     .map(role -> new SimpleGrantedAuthority(role.name()))
    //                     .toList();
    // }

    // @Override
    // public String getPassword() {
    //     return this.password;
    // }

    // @Override
    // public String getUsername() {
    //     return this.email;
    // }


    @PrePersist
    public void onPrepersist(){
        if (roles == null || roles.isEmpty()){
            roles = new HashSet<>();
            roles.add(Role.ROLE_USER);
        }
        this.actif = true;
    }

}
