package sn.thiordev221.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name="partages",
    uniqueConstraints={
        @UniqueConstraint(columnNames = {"invite_id", "todo_list_id"})
    }
)
public class Partage {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="invite_id", nullable=false)
    private Utilisateur invite;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="todo_list_id", nullable=false)
    private TodoList todoList;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Permission permission = Permission.ONLY_READ;

    private LocalDateTime datePartage;


    @PrePersist
    public void onPersist(){
        datePartage = LocalDateTime.now();
    }
    
}
