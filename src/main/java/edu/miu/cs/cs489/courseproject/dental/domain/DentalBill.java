package edu.miu.cs.cs489.courseproject.dental.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "dental_bills")
public class DentalBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BillStatus status;

    @Column(nullable = false)
    private String description;

    protected DentalBill() {
    }

    public DentalBill(Patient patient, Appointment appointment, LocalDate issueDate,
                      BigDecimal amount, BillStatus status, String description) {
        this.patient = patient;
        this.appointment = appointment;
        this.issueDate = issueDate;
        this.amount = amount;
        this.status = status;
        this.description = description;
    }

    public Long getBillId() {
        return billId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }
}
