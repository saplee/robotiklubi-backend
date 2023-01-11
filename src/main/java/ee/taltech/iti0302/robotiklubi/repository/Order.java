package ee.taltech.iti0302.robotiklubi.repository;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Table (name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client")
    private Client client;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "sliced")
    private Boolean sliced;
    @Column(name = "sliced_at")
    private OffsetDateTime slicedAt;
    @Column(name = "printed")
    private Boolean printed;
    @Column(name = "printed_at")
    private OffsetDateTime printedAt;
    @Column(name = "layer_count")
    private Integer layerCount;
    @Column(name = "layer_height")
    private Float layerHeight;
    @Column(name = "print_time")
    private Integer printTime;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "material_used")
    private Float materialUsed;
    @Column(name = "file_stl")
    private byte[] fileStl;
    @Column(name = "file_gcode")
    private byte[] fileGcode;
    @Column(name = "price")
    private Float price;


    @PrePersist
    public void saveCreationTime() {
        createdAt = OffsetDateTime.now();
    }

    @PreUpdate
    public void updateTimes() {
        if (Boolean.TRUE.equals(sliced) && slicedAt == null) {
            slicedAt = OffsetDateTime.now();
        }
        if (Boolean.TRUE.equals(printed) && printedAt == null) {
            printedAt = OffsetDateTime.now();
        }
    }
}
