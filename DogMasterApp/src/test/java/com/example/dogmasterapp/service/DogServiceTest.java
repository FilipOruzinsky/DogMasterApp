package com.example.dogmasterapp.service;

import com.example.dogmasterapp.entity.Dog;
import com.example.dogmasterapp.entity.User;
import com.example.dogmasterapp.exception.DogNotFoundException;
import com.example.dogmasterapp.repository.DogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogServiceTest {

    @Mock
    DogRepository dogRepository;

    @Mock
    UserService userService;

    @InjectMocks
    DogService dogService;

    private Dog dog(String name, int age, String breed, User owner) {
        Dog d = new Dog();
        d.setName(name);
        d.setAge(age);
        d.setBreed(breed);
        d.setOwner(owner);
        return d;
    }

    private User user(String id) {
        User u = new User();
        u.setUserID(id);
        return u;
    }

    @Test
    @DisplayName("createDog: nastaví ownera z current usera a uloží psa")
    void createDog_setsOwnerAndSaves() {
        User current = user("u-123");
        Dog input = dog("Rex", 3, "beagle", null);
        Dog saved = dog("Rex", 3, "beagle", current);

        when(userService.getCurrentUser()).thenReturn(current);
        when(dogRepository.save(any(Dog.class))).thenReturn(saved);

        Dog result = dogService.createDog(input);

        ArgumentCaptor<Dog> toSave = ArgumentCaptor.forClass(Dog.class);
        verify(dogRepository).save(toSave.capture());
        assertThat(toSave.getValue().getOwner()).isSameAs(current);

        assertThat(result).isSameAs(saved);
        verifyNoMoreInteractions(dogRepository, userService);
    }

    @Test
    @DisplayName("getDogById: nájde a vráti psa")
    void getDogById_found() {
        Dog rex = dog("Rex", 3, "beagle", null);
        when(dogRepository.findById(42)).thenReturn(Optional.of(rex));

        Dog result = dogService.getDogById(42);

        assertThat(result).isSameAs(rex);
        verify(dogRepository).findById(42);
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("getDogById: keď nenájde, hodí DogNotFoundException")
    void getDogById_notFound() {
        when(dogRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dogService.getDogById(99))
                .isInstanceOf(DogNotFoundException.class)
                .hasMessageContaining("99");
        verify(dogRepository).findById(99);
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("getAllDogs: vráti všetkých psov")
    void getAllDogs_returnsAll() {
        List<Dog> dogs = Arrays.asList(dog("A", 1, "x", null), dog("B", 2, "y", null));
        when(dogRepository.findAll()).thenReturn(dogs);

        List<Dog> result = dogService.getAllDogs();

        assertThat(result).containsExactlyElementsOf(dogs);
        verify(dogRepository).findAll();
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("getDogsByOwner: vráti psov podľa ownera")
    void getDogsByOwner_returnsByOwner() {
        String ownerId = "owner-007";
        List<Dog> dogs = List.of(dog("A", 1, "x", null));
        when(dogRepository.findAllByOwner_UserID(ownerId)).thenReturn(dogs);

        List<Dog> result = dogService.getDogsByOwner(ownerId);

        assertThat(result).isSameAs(dogs);
        verify(dogRepository).findAllByOwner_UserID(ownerId);
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("updateDog: aktualizuje polia a uloží existujúceho psa")
    void updateDog_updatesAndSaves() {
        Dog existing = dog("Old", 5, "husky", null);
        Dog patch = dog("NewName", 2, "corgi", null);

        when(dogRepository.findById(10)).thenReturn(Optional.of(existing));
        when(dogRepository.save(any(Dog.class))).thenAnswer(inv -> inv.getArgument(0));

        Dog result = dogService.updateDog(10, patch);

        assertThat(existing.getName()).isEqualTo("NewName");
        assertThat(existing.getAge()).isEqualTo(2);
        assertThat(existing.getBreed()).isEqualTo("corgi");
        assertThat(result).isSameAs(existing);

        verify(dogRepository).findById(10);
        verify(dogRepository).save(existing);
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("updateDog: keď neexistuje, hodí DogNotFoundException")
    void updateDog_notFound() {
        when(dogRepository.findById(11)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dogService.updateDog(11, new Dog()))
                .isInstanceOf(DogNotFoundException.class);

        verify(dogRepository).findById(11);
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("deleteDogById: nájde a vymaže psa")
    void deleteDogById_deletes() {
        Dog existing = dog("ToDelete", 1, "mix", null);
        when(dogRepository.findById(7)).thenReturn(Optional.of(existing));

        dogService.deleteDogById(7);

        verify(dogRepository).findById(7);
        verify(dogRepository).delete(existing);
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("deleteDogById: keď neexistuje, hodí DogNotFoundException")
    void deleteDogById_notFound() {
        when(dogRepository.findById(8)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dogService.deleteDogById(8))
                .isInstanceOf(DogNotFoundException.class);

        verify(dogRepository).findById(8);
        verifyNoMoreInteractions(dogRepository);
        verifyNoInteractions(userService);
    }

    @Nested
    @DisplayName("changeOwner")
    class ChangeOwnerTests {

        @Test
        @DisplayName("zmení ownera na current usera a uloží")
        void changeOwner_setsCurrentUserAndSaves() {
            User current = user("cur-1");
            Dog existing = dog("Buddy", 4, "golden", user("old-9"));

            when(dogRepository.findById(55)).thenReturn(Optional.of(existing));
            when(userService.getCurrentUser()).thenReturn(current);
            when(dogRepository.save(any(Dog.class))).thenAnswer(inv -> inv.getArgument(0));

            Dog result = dogService.changeOwner(55);

            assertThat(existing.getOwner()).isSameAs(current);
            assertThat(result).isSameAs(existing);

            verify(dogRepository).findById(55);
            verify(userService).getCurrentUser();
            verify(dogRepository).save(existing);
            verifyNoMoreInteractions(dogRepository, userService);
        }

        @Test
        @DisplayName("ak psa nenájde, hodí DogNotFoundException")
        void changeOwner_notFound() {
            when(dogRepository.findById(56)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> dogService.changeOwner(56))
                    .isInstanceOf(DogNotFoundException.class);

            verify(dogRepository).findById(56);
            verifyNoMoreInteractions(dogRepository);
            verifyNoInteractions(userService);
        }
    }
}