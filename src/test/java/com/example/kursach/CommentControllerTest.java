package com.example.kursach;


import com.example.kursach.controllers.CommentController;
import com.example.kursach.models.Comment;
import com.example.kursach.models.Place;
import com.example.kursach.models.User;
import com.example.kursach.services.CommentService;
import com.example.kursach.services.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private CommentController commentController;

    private MockHttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    public void testGetCommentsForPlace_AuthorizedUser() throws Exception {
        // Подготовка данных
        Long placeId = 1L;
        User currentUser = new User();
        Place currentPlace = new Place();

        // Настраиваем поведение мока
        when(placeService.findPlaceById(placeId)).thenReturn(currentPlace);
        when(commentService.getCommentsByPlaceId(placeId)).thenReturn(new ArrayList<Comment>());

        // Помещаем пользователя в сессию
        session.setAttribute("user", currentUser);

        // Выполняем запрос
        mockMvc.perform(get("/comments?id=" + placeId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(model().attributeExists("newComment"))
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("place", currentPlace));
    }

    @Test
    public void testGetCommentsForPlace_UnauthorizedUser() throws Exception {
        // Выполнение GET-запроса без авторизованного пользователя
        mockMvc.perform(get("/comments?id=1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testAddComment_Successful() throws Exception {
        Long placeId = 1L;
        String commentText = "This is a test comment.";
        User currentUser = new User(); // Создаем тестового пользователя
        Place currentPlace = new Place(); // Создаем тестовое место

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(placeService.findPlaceById(placeId)).thenReturn(currentPlace);

        // Выполняем запрос на добавление комментария
        mockMvc.perform(post("/comments/add")
                        .param("placeId", String.valueOf(placeId))
                        .param("text", commentText)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));

        // Проверяем, что комментарий был сохранён
        Comment newComment = new Comment();
        newComment.setText(commentText);
        newComment.setUser(currentUser);
        newComment.setPlace(currentPlace);

        verify(commentService).saveComment(newComment);
    }

    @Test
    public void testAddComment_EmptyText() throws Exception {
        Long placeId = 1L;
        String commentText = ""; // Пустой текст комментария
        User currentUser = new User(); // Создаем тестового пользователя
        Place currentPlace = new Place(); // Создаем тестовое место

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(placeService.findPlaceById(placeId)).thenReturn(currentPlace);

        // Выполняем запрос на добавление комментария
        mockMvc.perform(post("/comments/add")
                        .param("placeId", String.valueOf(placeId))
                        .param("text", commentText)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));
        // Проверяем, что метод сохранения комментария не был вызван
        verify(commentService, never()).saveComment(any(Comment.class));
    }

    @Test
    public void testAddComment_NoUser() throws Exception {
        Long placeId = 1L;
        String commentText = "This comment should not be added.";

        // Выполняем запрос без авторизации
        mockMvc.perform(post("/comments/add")
                        .param("placeId", String.valueOf(placeId))
                        .param("text", commentText)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login")); // Ожидаем перенаправление на страницу логина

        // Проверяем, что метод сохранения комментария не был вызван
        verify(commentService, never()).saveComment(any(Comment.class));
    }
    @Test
    public void testUpdateComment_Successful() throws Exception {
        Long commentId = 1L;
        Long placeId = 1L;
        String newText = "Updated comment text";
        User currentUser = new User(); // Создаем тестового пользователя
        currentUser.setUsername("testUser");

        Comment existingComment = new Comment(); // Создаем существующий комментарий
        existingComment.setUser(currentUser); // Назначаем его создателем комментария

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(commentService.getCommentById(commentId)).thenReturn(existingComment);

        // Выполняем запрос на обновление комментария
        mockMvc.perform(patch("/comments/update")
                        .param("commentId", String.valueOf(commentId))
                        .param("placeId", String.valueOf(placeId))
                        .param("newText", newText)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));

        // Проверяем, что метод обновления комментария был вызван
        verify(commentService).updateComment(commentId, newText);
    }

    @Test
    public void testUpdateComment_EmptyText() throws Exception {
        Long commentId = 1L;
        Long placeId = 1L;
        String newText = ""; // Пустой текст комментария
        User currentUser = new User(); // Создаем тестового пользователя
        currentUser.setUsername("testUser");

        Comment existingComment = new Comment(); // Создаем существующий комментарий
        existingComment.setUser(currentUser); // Назначаем его создателем комментария

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(commentService.getCommentById(commentId)).thenReturn(existingComment);

        // Выполняем запрос на обновление комментария
        mockMvc.perform(patch("/comments/update")
                .param("commentId", String.valueOf(commentId))
                        .param("placeId", String.valueOf(placeId))
                        .param("newText", newText)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));

        // Проверяем, что метод обновления комментария не был вызван
        verify(commentService, never()).updateComment(anyLong(), anyString());
    }

    @Test
    public void testUpdateComment_NotCommentOwner() throws Exception {
        Long commentId = 1L;
        Long placeId = 1L;
        String newText = "This should not be updated.";
        User currentUser = new User(); // Создаем тестового пользователя
        currentUser.setUsername("testUser");

        User otherUser = new User(); // Создаем другого пользователя
        otherUser.setUsername("otherUser");

        Comment existingComment = new Comment(); // Создаем существующий комментарий
        existingComment.setUser(otherUser); // Назначаем его создателем комментария

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(commentService.getCommentById(commentId)).thenReturn(existingComment);

        // Выполняем запрос на обновление комментария
        mockMvc.perform(patch("/comments/update")
                        .param("commentId", String.valueOf(commentId))
                        .param("placeId", String.valueOf(placeId))
                        .param("newText", newText)
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));

        // Проверяем, что метод обновления комментария не был вызван
        verify(commentService, never()).updateComment(anyLong(), anyString());
    }

    @Test
    public void testDeleteComment_Successful() throws Exception {
        Long commentId = 1L;
        Long placeId = 1L;
        User currentUser = new User(); // Создаем тестового пользователя
        currentUser.setUsername("testUser");

        Comment existingComment = new Comment(); // Создаем существующий комментарий
        existingComment.setUser(currentUser); // Назначаем его создателем комментария

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(commentService.getCommentById(commentId)).thenReturn(existingComment);

        // Выполняем запрос на удаление комментария
        mockMvc.perform(delete("/comments/delete")
                        .param("commentId", String.valueOf(commentId))
                        .param("placeId", String.valueOf(placeId))
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));

        // Проверяем, что метод удаления комментария был вызван
        verify(commentService).deleteComment(commentId);
    }

    @Test
    public void testDeleteComment_NotCommentOwner() throws Exception {
        Long commentId = 1L;
        Long placeId = 1L;
        User currentUser = new User(); // Создаем тестового пользователя
        currentUser.setUsername("testUser");

        User otherUser = new User(); // Создаем другого пользователя
        otherUser.setUsername("otherUser");

        Comment existingComment = new Comment(); // Создаем существующий комментарий
        existingComment.setUser(otherUser); // Назначаем его создателем комментария

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(commentService.getCommentById(commentId)).thenReturn(existingComment);

        // Выполняем запрос на удаление комментария
        mockMvc.perform(delete("/comments/delete")
                .param("commentId", String.valueOf(commentId))
                .param("placeId", String.valueOf(placeId))
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));

        // Проверяем, что метод удаления комментария не был вызван
        verify(commentService, never()).deleteComment(anyLong());
    }

    @Test
    public void testDeleteComment_NonExistentComment() throws Exception {
        Long commentId = 1L;
        Long placeId = 1L;
        User currentUser = new User(); // Создаем тестового пользователя
        currentUser.setUsername("testUser");

        // Настраиваем поведение мока
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(commentService.getCommentById(commentId)).thenReturn(null); // Комментарий не существует

        // Выполняем запрос на удаление комментария
        mockMvc.perform(delete("/comments/delete")
                        .param("commentId", String.valueOf(commentId))
                        .param("placeId", String.valueOf(placeId))
                        .session(session)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comments?id=" + placeId));

        // Проверяем, что метод удаления комментария не был вызван
        verify(commentService, never()).deleteComment(anyLong());
    }
}
}
