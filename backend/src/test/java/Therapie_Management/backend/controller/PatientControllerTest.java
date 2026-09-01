package Therapie_Management.backend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import Therapie_Management.backend.entity.Role;
import Therapie_Management.backend.security.JwtTokenProvider;

/**
 * Testet die komplette Sicherheitskette end-to-end (echter JwtAuthenticationFilter + echtes
 * @PreAuthorize auf PatientController), nicht nur den Controller isoliert mit gemockter
 * Security - genau die Frage, die in der Praesentation am ehesten kommt: "Wie wird
 * verhindert, dass ein Patient auf fremde Daten oder Therapeuten-Funktionen zugreift?"
 *
 * Die Test-User (v110001/v110002/v220001) kommen aus data.sql, das bei jedem Testlauf
 * frisch in die H2-Datenbank geladen wird (ddl-auto=create-drop).
 */
@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void getPatient_ohneToken_gibt401() throws Exception {
        mockMvc.perform(get("/api/patients/v110001"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getPatient_alsFremderPatient_gibt403() throws Exception {
        String tokenVonMaria = jwtTokenProvider.generateToken("v110002", Role.PATIENT);

        mockMvc.perform(get("/api/patients/v110001").header("Authorization", "Bearer " + tokenVonMaria))
            .andExpect(status().isForbidden());
    }

    @Test
    void getPatient_alsTherapeut_gibt403_trotzGueltigemToken() throws Exception {
        String tokenVonTherapeut = jwtTokenProvider.generateToken("v220001", Role.THERAPEUT);

        mockMvc.perform(get("/api/patients/v110001").header("Authorization", "Bearer " + tokenVonTherapeut))
            .andExpect(status().isForbidden());
    }

    @Test
    void getPatient_alsEigenerPatient_gibt200() throws Exception {
        String tokenVonElena = jwtTokenProvider.generateToken("v110001", Role.PATIENT);

        mockMvc.perform(get("/api/patients/v110001").header("Authorization", "Bearer " + tokenVonElena))
            .andExpect(status().isOk());
    }
}
