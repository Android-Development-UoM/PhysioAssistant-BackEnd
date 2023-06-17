package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Admin;
import uom.backend.physioassistant.repositories.AdminUserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

class AdminUserServiceTest {
    @Mock
    private AdminUserRepository adminUserRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAdmins() {
        // Given
        Admin admin1 = new Admin();
        Admin admin2 = new Admin();
        when(adminUserRepository.findAll()).thenReturn(Arrays.asList(admin1, admin2));

        // When
        Collection<Admin> admins = adminUserService.getAllAdmins();

        // Then
        assertThat(admins).containsExactlyInAnyOrder(admin1, admin2);
    }

    @Test
    void testGetAdminById_ExistingId_ShouldReturnAdmin() {
        // Given
        String id = "123";
        Admin admin = new Admin();
        when(adminUserRepository.findById(id)).thenReturn(Optional.of(admin));

        // When
        Admin result = adminUserService.getAdminById(id);

        // Then
        assertThat(result).isEqualTo(admin);
    }

    @Test
    void testGetAdminById_NonExistingId_ShouldThrowEntityNotFoundException() {
        // Given
        String id = "123";
        when(adminUserRepository.findById(id)).thenReturn(Optional.empty());

        // When and Then
        assertThatThrownBy(() -> adminUserService.getAdminById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with id: " + id + " not found.");
    }

    @Test
    void testCheckIfUserExists_ExistingUsername_ShouldReturnTrue() {
        // Given
        String username = "testuser_true";
        when(adminUserRepository.findById(username)).thenReturn(Optional.of(new Admin()));

        // When
        boolean result = adminUserService.checkIfUserExists(username);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testCheckIfUserExists_NonExistingUsername_ShouldReturnFalse() {
        // Given
        String username = "testuser_false";
        when(adminUserRepository.findById(username)).thenReturn(Optional.empty());

        // When
        boolean result = adminUserService.checkIfUserExists(username);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testAddAdminUser_NewUser_ShouldReturnAddedUser() {
        // Given
        Admin newUser = new Admin();
        newUser.setUsername("newuser");
        when(adminUserRepository.findById(newUser.getUsername())).thenReturn(Optional.empty());
        when(adminUserRepository.save(newUser)).thenReturn(newUser);

        // When
        Admin result = adminUserService.addAdminUser(newUser);

        // Then
        assertThat(result).isEqualTo(newUser);
    }

    @Test
    void testAddAdminUser_ExistingUser_ShouldThrowAlreadyAddedException() {
        // Given
        Admin existingUser = new Admin();
        existingUser.setUsername("existinguser");
        when(adminUserRepository.findById(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        // When and Then
        assertThatThrownBy(() -> adminUserService.addAdminUser(existingUser))
                .isInstanceOf(AlreadyAddedException.class)
                .hasMessage("Username: " + existingUser.getUsername() + " is taken");
    }
}