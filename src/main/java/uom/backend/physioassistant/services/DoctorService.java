package uom.backend.physioassistant.services;

import org.springframework.stereotype.Service;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.exceptions.NotFoundException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.repositories.DoctorRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Collection<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getById(String id) {
        return doctorRepository.findById(id);
    }

    public void deleteById(String id) {
        Optional<Doctor> foundDoctor = this.getById(id);

        // User not found
        if (foundDoctor.isEmpty())
            throw new NotFoundException("User with id: " + id + " not found.");

        this.doctorRepository.delete(foundDoctor.get());
    }

    public Doctor createDoctor(Doctor doctor) {
        String givenAFM = doctor.getAfm();
        Optional<Doctor> foundDoctor = this.doctorRepository.findById(givenAFM);

        if (foundDoctor.isPresent())
            throw new AlreadyAddedException("Doctor with AFM: " + givenAFM + " is already added.");

        return doctorRepository.save(doctor);
    }
}
