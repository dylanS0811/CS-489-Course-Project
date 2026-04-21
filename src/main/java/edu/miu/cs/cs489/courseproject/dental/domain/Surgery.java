package edu.miu.cs.cs489.courseproject.dental.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "surgeries")
public class Surgery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surgeryId;

    @Column(nullable = false, unique = true, length = 16)
    private String surgeryNumber;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false, length = 32)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private Address locationAddress;

    @OneToMany(mappedBy = "surgery")
    private List<Appointment> appointments = new ArrayList<>();

    protected Surgery() {
    }

    public Surgery(String surgeryNumber, String name, String phoneNumber, Address locationAddress) {
        this.surgeryNumber = surgeryNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.locationAddress = locationAddress;
    }

    public Long getSurgeryId() {
        return surgeryId;
    }

    public String getSurgeryNumber() {
        return surgeryNumber;
    }

    public void setSurgeryNumber(String surgeryNumber) {
        this.surgeryNumber = surgeryNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(Address locationAddress) {
        this.locationAddress = locationAddress;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}
