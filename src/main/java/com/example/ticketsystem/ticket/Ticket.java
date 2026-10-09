package com.example.ticketsystem.ticket;

import com.example.ticketsystem.vehicle.Vehicle;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ticket",
        uniqueConstraints = @UniqueConstraint(name = "uk_ticket_code", columnNames = "code"),
        indexes = @Index(name = "idx_ticket_vehicle", columnList = "vehicle_id"))
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 50)
    private Long id;


    @NotNull
    @Column(name = "code", nullable = false, length = 36, updatable = false)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(name = "passenger_name", nullable = false, length = 100, updatable = false)
    private String passengerName;

    @NotNull
    @Column(name = "issued_at", nullable = false, updatable = false)
    private Instant issuedAt;

    @Column(name = "validated", nullable = false)
    private boolean validated;

    @Column(name = "validated_at")
    private Instant validatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_ticket_vehicle"))
    private Vehicle vehicle;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    protected Ticket() {
    }

    public Ticket(Vehicle vehicle, String passengerName, Instant issuedAt) {
        this.code = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.passengerName = passengerName;
        this.issuedAt = issuedAt;
        this.validated = false;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getPassengerName() { return passengerName; }
    public Instant getIssuedAt() { return issuedAt; }
    public boolean isValidated() { return validated; }
    public Instant getValidatedAt() { return validatedAt; }
    public Vehicle getVehicle() { return vehicle; }

    public void validate(Instant now){
        if(validated){
            throw new TicketAlreadyValidatedException(code);
        }
        validated = true;
        validatedAt = now;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        return Objects.equals(code, ((Ticket) o).code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
