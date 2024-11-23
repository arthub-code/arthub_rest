package br.com.arthub.ah_rest_comission.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.arthub.ah_rest_comission.domain.enums.DeliverableStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "deliverables")
@Data
public class DeliverableEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID deliverableId;
    
    @ManyToOne
    @JoinColumn(name = "comission_fk", nullable = false)
    private ComissionEntity comissionParent;
    
    @Column(nullable = false, length = 60)
    private String name;                                       // OBRIGATORIO - Nome descritivo do entregável (ex: "Esboço inicial")
    
    @Lob
    private String description;                                // OPCIONAL - Descrição detalhada da entrega
    
    @Column(name = "delivered_at", nullable = false)
    private LocalDateTime deliveredAt;                         // OBRIGATORIO - Data em que o entregável foi entregue
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliverableStatus status;                          // OBRIGATORIO - Status atual do entregável (ex: "Pendente", "Concluído")
    
    @Column(name = "revision_number", nullable = false)
    private int revisionNumber;                                // OBRIGATORIO - Número da revisão, útil se houver múltiplas revisões do mesmo item
    
    @Column(name = "expected_delivery_date", nullable = false)
    private LocalDateTime expectedDeliveryDate;                // OBRIGATORIO - Data estimada para entrega

    @Lob
    @Column(name = "file_content")
    private byte[] fileContent;                                // OPCIONAL - Conteúdo do arquivo entregue (caso o arquivo esteja diretamente armazenado no BD)
    
    @Column(name = "file_url")
    private String fileUrl;                                    // OPCIONAL - URL do arquivo entregue (caso os arquivos estejam em armazenamento externo)

    @Column(name = "file_type")
    private String fileType;                                   // OPCIONAL - Tipo do arquivo (ex: "PNG", "PDF")
    
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;                          // OBRIGATORIO - Valor associado à entrega, caso seja pago por etapa ou entregável

    @Column(name = "artist_id", nullable = false)
    private UUID artist;                                       // OBRIGATORIO - Artista responsável pela entrega
}
