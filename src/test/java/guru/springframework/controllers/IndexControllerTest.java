package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeServiceImpl recipeService;

    @Mock
    Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        indexController = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    public void getIndexPage() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(new Recipe());
        recipesData.add(recipe);
        Mockito.when(recipeService.getRecipes()).thenReturn(recipesData);
//        Mockito.when(model.addAttribute("recipes", recipesData)).thenReturn(model);
        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String indexPage = indexController.getIndexPage(model);

        //then
        assertEquals("index", indexPage);
        Mockito.verify(recipeService, Mockito.times(1)).getRecipes();

        //Three ways to do one thing
        Mockito.verify(model, Mockito.times(1)).addAttribute("recipes", recipesData);
        Mockito.verify(model, Mockito.times(1)).addAttribute(anyString(), Mockito.anySet());
        Mockito.verify(model, Mockito.times(1)).addAttribute(eq("recipes"), Mockito.anySet());
        Mockito.verify(model, Mockito.times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2, setInController.size());



    }
}