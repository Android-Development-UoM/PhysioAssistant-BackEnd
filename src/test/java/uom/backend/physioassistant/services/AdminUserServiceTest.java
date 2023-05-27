package uom.backend.physioassistant.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uom.backend.physioassistant.models.users.Admin;
import uom.backend.physioassistant.repositories.AdminUserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {
    @Mock
    AdminUserRepository adminUserRepository;
    AdminUserService classUnderTest;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println("----- Test " + testInfo.getDisplayName() + " Started -----");
        classUnderTest = new AdminUserService(adminUserRepository);
    }

    @AfterEach
    void tearDown() {
        System.out.println("---- Test Completed ----" + System.lineSeparator());
    }

    @Test
    void shouldGetAllAdmins() {
        // WHEN
        classUnderTest.getAllAdmins();
        // THEN
        verify(adminUserRepository).findAll();
    }

    @Test
    void shouldGetAdminByID() {
        // Given
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");

        given(adminUserRepository.findById(any(String.class)))
                .willReturn(Optional.of(admin));

        // When
        Admin foundAdmin = classUnderTest.getAdminById("admin");

        // Then
        assertThat(foundAdmin).isEqualTo(admin);
    }

    @Test
    void shouldCheckIfUserExistsAndReturnTrue() {
        // Given
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");

        given(adminUserRepository.findById(any(String.class)))
                .willReturn(Optional.of(admin));

        // When
        boolean userExists = classUnderTest.checkIfUserExists("admin");

        // Then
        assertThat(userExists).isTrue();
    }

    @Test
    void shouldCheckIfUserExistsAndReturnFalse() {
        // Given
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");

        given(adminUserRepository.findById(any(String.class)))
                .willReturn(Optional.empty());

        // When
        boolean userExists = classUnderTest.checkIfUserExists("admin");

        // Then
        assertThat(userExists).isFalse();
    }

    @Test
    void shouldAddAdminWithoutException() {
        // Given
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("admin");

        // When
        classUnderTest.addAdminUser(admin);

        // Then
        ArgumentCaptor<Admin> adminArgumentCaptor =
                ArgumentCaptor.forClass(Admin.class);

        verify(adminUserRepository)
                .save(adminArgumentCaptor.capture());

        Admin capturedAdmin = adminArgumentCaptor.getValue();
        assertThat(admin).isEqualTo(capturedAdmin);
    }
}