package sn.thiordev221.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="todo_lists")
public class TodoList {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String titre;

    @Column
    private String description;

    @Column(updatable=false)
    private LocalDateTime dateCreation;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy="todoList", cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private List<Tache> taches = new ArrayList<>();

    @OneToMany(mappedBy="todoList", cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private List<Partage> partages = new ArrayList<>();
    
}
