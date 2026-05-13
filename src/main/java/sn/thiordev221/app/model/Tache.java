package sn.thiordev221.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name="taches")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String titre;

    @Column
    private String description;

    @Column(nullable=false)
    private Boolean termine;

    @Column
    private LocalDateTime echeance;

    @ManyToOne
    @JoinColumn(name="todo_list_id", nullable=false)
    private TodoList todoList;

    @PrePersist
    public void onPersist(){
        if (termine == null) termine = false;
        echeance = LocalDateTime.now();
        termine = false;
    }
    
}
