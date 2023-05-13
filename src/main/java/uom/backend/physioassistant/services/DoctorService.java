package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
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

    public Doctor getById(String id) {
        Optional<Doctor> foundDoctor = this.doctorRepository.findById(id);

        if (foundDoctor.isEmpty())
            throw new EntityNotFoundException("Doctor with id: " + id + " not found.");

        return foundDoctor.get();
    }

    public Optional<Doctor> getOptionalById(String id) {
        return this.doctorRepository.findById(id);
    }

    public void deleteById(String id) {
        Doctor foundDoctor = this.getById(id);

        this.doctorRepository.delete(foundDoctor);
    }
    


    public Doctor createDoctor(Doctor doctor) {
        String givenAFM = doctor.getUsername();
        Optional<Doctor> foundDoctor = this.doctorRepository.findById(givenAFM);

        if (foundDoctor.isPresent())
            throw new AlreadyAddedException("Doctor with AFM: " + givenAFM + " is already added.");

        return doctorRepository.save(doctor);
    }
}
