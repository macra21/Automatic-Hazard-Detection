package org.example.ahd.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "response_time")
public class ResponseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "hazard_id")
    private Hazard hazard;
    private LocalDateTime initial_date;
    private LocalDateTime update_date;

    @Enumerated(EnumType.STRING)
    private HazardStatus initial_status;

    @Enumerated(EnumType.STRING)
    private HazardStatus update_status;

    private Double response_time;

    public ResponseTime() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Hazard getHazard() {
        return hazard;
    }

    public void setHazard(Hazard hazard) {
        this.hazard = hazard;
    }

    public LocalDateTime getInitial_date() {
        return initial_date;
    }

    public void setInitial_date(LocalDateTime initial_date) {
        this.initial_date = initial_date;
    }

    public LocalDateTime getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(LocalDateTime update_date) {
        this.update_date = update_date;
    }

    public HazardStatus getInitial_status() {
        return initial_status;
    }

    public void setInitial_status(HazardStatus initial_status) {
        this.initial_status = initial_status;
    }

    public HazardStatus getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(HazardStatus update_status) {
        this.update_status = update_status;
    }

    public Double getResponse_time() {
        return response_time;
    }

    public void setResponse_time(Double response_time) {
        this.response_time = response_time;
    }
}
