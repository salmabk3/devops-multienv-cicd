package com.devops.task_api.service;

import com.devops.task_api.model.Task;
import com.devops.task_api.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //relier Junit5 et Mockito
public class TaskServiceTest {

     @Mock
    private TaskRepository taskRepository; //mockito crée un faux taskrepository

     @InjectMocks
    private TaskService taskService;//mockito crée une instance réelle de TaskService
    //et essaie d'y injecter le faux repository(données)

     private Task task;

     //pour éviter la création de la tâche dans chaque test
     @BeforeEach
    void setUp(){
         task = new Task();
         task.setId(1L);
         task.setTitle("Learn Docker");
         task.setDescription("Docker basics");
         task.setStatus("TODO");
     }

     //test 1: récupérer toutes les tâches
    @Test
    void shouldGetAllTasks(){
         //1.préparer le mock
        //le service appelle taskrepository.findAll() retourne une liste contenant une tâche
         when(taskRepository.findAll())
                 .thenReturn(List.of(task));

         //2. executer la méthode réelle
        List<Task> result = taskService.getAllTasks();

        //3.vérifier le résultat
        assertEquals(1, result.size()); //vérifie que la liste contient exactement une tache
        assertEquals("Learn Docker", result.get(0).getTitle());//verifie que le titre de la 1ere tache est Learn Docker

        verify(taskRepository).findAll();//verifie un appel de méthode, verifie que le service a appelé
        //taskrepository exact 1 fois

    }

    //test2: récuperer une tache par ID:1
    @Test
    void shouldGetTaskById(){

        //Quand repository.findById(1L) est appelé,
        //retourne notre objet task au lieu d'interroger PostgreSQL.
         when(taskRepository.findById(1L))
                 .thenReturn(Optional.of(task)); //signifie qu'une valeur est présente
        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Learn Docker", result.getTitle());
        verify(taskRepository).findById(1L);


    }

    //test 3: vérifier l'erreur 404

    @Test
    void shouldThrow404WhenTaskNotFound(){

         when(taskRepository.findById(99L))
                 .thenReturn(Optional.empty());

        //assertThrows vérifie que l'exécution provoque l'exception attendue
         ResponseStatusException exception = assertThrows(
                 ResponseStatusException.class,
                 () -> taskService.getTaskById(99L)
         );
         //Vérifie que l'exception indique bien le statut HTTP 404
         assertEquals(HttpStatus.NOT_FOUND,
                 exception.getStatusCode());
    }

    //test4: créer une tache
    @Test
    void shouldCreateTask(){

         when(taskRepository.save(any(Task.class))).thenAnswer(invocation ->
                 invocation.getArgument(0));

         Task result= taskService.createTask(task);

         assertEquals("Learn Docker", result.getTitle());
         assertEquals("TODO", result.getStatus());
         assertNotNull(result.getCreatedAt());

         verify(taskRepository).save(any(Task.class));


     }

     //test5 : modifier une tache
    @Test
    void shouldUpdateTask(){
            //on simule la modif d'une tache existante
         Task updatedTask= new Task();
         updatedTask.setTitle("Learn Kubernetes");
         updatedTask.setDescription("Kubernetes basics");
         updatedTask.setStatus("DONE");

         when(taskRepository.findById(1L)) //retourne ma tache actuelle
                 .thenReturn(Optional.of(task));

         when(taskRepository.save(any(Task.class))).thenAnswer(
                 invocation -> invocation.getArgument(0));

         Task result= taskService.updateTask(1L,updatedTask);

         assertEquals("Learn Kubernetes", result.getTitle());
         assertEquals("DONE", result.getStatus());
//Vérifie que le repository a reçu l'objet task initialement récupéré,
// dont les attributs ont normalement été modifiés.
         verify(taskRepository).save(task);
    }

    //test6: supprimer une tache
    @Test
    void shouldDeleteTask() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task)); //retourne une tache existante

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);//mockito vérifie que repository.delete(task) a été appelé
    }

    //test7: supprime une tache inexistante
    @Test
    void shouldThrow404WhenDeletingUnknownTask() {
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        //le service doit signaler un erreur 404
        //lorsque la tache n'existe pas
        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> taskService.deleteTask(99L)
                );

        assertEquals(HttpStatus.NOT_FOUND,
                exception.getStatusCode());
//Vérifie que la méthode delete() n'a jamais
// été appelée avec une tâche correspondante.
        verify(taskRepository, never()).delete(any(Task.class));
    }
}
