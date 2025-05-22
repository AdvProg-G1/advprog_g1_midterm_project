package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceOrderServiceImplTest {

    @Mock
    ServiceOrderRepository repo;

    @InjectMocks
    ServiceOrderServiceImpl service;

    ServiceOrder sample;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sample = ServiceOrder.builder()
                .id(UUID.randomUUID())
                .itemName("Laptop")
                .condition("Broken Screen")
                .problemDescription("Cracked")
                .technicianId("tech-1")
                .userId("user-1")
                .serviceDate(LocalDate.now().plusDays(1))
                .paymentMethod("CREDIT_CARD")
                .couponApplied(false)
                .status("WAITING_CONFIRMATION")
                .estimatedCompletionTime(null)
                .estimatedPrice(null)
                .build();
    }

    @Test
    void createOrder_whenStatusNull_assignsDefault() {
        ServiceOrder noStatus = ServiceOrder.builder()
                .itemName("X").condition("Y").problemDescription("Z")
                .technicianId("t").userId("u")
                .serviceDate(LocalDate.now().plusDays(2))
                .paymentMethod("CASH").couponApplied(false)
                .status(null)
                .build();

        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        ServiceOrder created = service.createOrder(noStatus);

        assertThat(created.getStatus()).isEqualTo("WAITING_CONFIRMATION");
        verify(repo).save(created);
    }

    @Test
    void createOrder_whenStatusPresent_keepsIt() {
        sample.setStatus("IN_PROGRESS");
        when(repo.save(sample)).thenReturn(sample);

        ServiceOrder out = service.createOrder(sample);
        assertThat(out.getStatus()).isEqualTo("IN_PROGRESS");
        verify(repo).save(sample);
    }

    @Test
    void getAllOrders_delegatesToRepo() {
        List<ServiceOrder> list = List.of(sample);
        when(repo.findAll()).thenReturn(list);

        List<ServiceOrder> out = service.getAllOrders();
        assertThat(out).isSameAs(list);
        verify(repo).findAll();
    }

    @Test
    void getOrderById_found_returnsOrder() {
        when(repo.findById(sample.getId())).thenReturn(Optional.of(sample));

        ServiceOrder out = service.getOrderById(sample.getId());
        assertThat(out).isEqualTo(sample);
    }

    @Test
    void getOrderById_notFound_returnsNull() {
        when(repo.findById(any())).thenReturn(Optional.empty());

        ServiceOrder out = service.getOrderById(UUID.randomUUID());
        assertThat(out).isNull();
    }

    @Test
    void findOrdersByTechnicianId_usesRepo() {
        when(repo.findByTechnicianId("tech-1")).thenReturn(List.of(sample));

        List<ServiceOrder> out = service.findOrdersByTechnicianId("tech-1");
        assertThat(out).containsExactly(sample);
    }

    @Test
    void findOrdersByUserId_usesRepo() {
        when(repo.findByUserId("user-1")).thenReturn(List.of(sample));

        List<ServiceOrder> out = service.findOrdersByUserId("user-1");
        assertThat(out).containsExactly(sample);
    }

    @Test
    void update_existing_savesAndReturns() {
        UUID id = sample.getId();
        ServiceOrder updated = ServiceOrder.builder()
                .itemName("New").condition("C").problemDescription("D")
                .serviceDate(LocalDate.now().plusDays(3))
                .paymentMethod("BANK_TRANSFER")
                .couponApplied(true)
                .technicianId("tech-2")
                .estimatedCompletionTime("3 days")
                .estimatedPrice(500_00)
                .status("IN_PROGRESS")
                .build();

        when(repo.findById(id)).thenReturn(Optional.of(sample));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        Optional<ServiceOrder> out = service.update(id, updated);

        assertThat(out).isPresent();
        ServiceOrder o = out.get();
        assertThat(o.getItemName()).isEqualTo("New");
        assertThat(o.getEstimatedPrice()).isEqualTo(500_00);
        verify(repo).save(o);
    }

    @Test
    void update_missing_returnsEmpty() {
        when(repo.findById(any())).thenReturn(Optional.empty());
        Optional<ServiceOrder> out = service.update(UUID.randomUUID(), sample);
        assertThat(out).isEmpty();
        verify(repo, never()).save(any());
    }

    @Test
    void delete_existing_returnsTrue() {
        when(repo.findById(sample.getId())).thenReturn(Optional.of(sample));
        boolean deleted = service.delete(sample.getId());
        assertThat(deleted).isTrue();
        verify(repo).delete(sample);
    }

    @Test
    void delete_missing_returnsFalse() {
        when(repo.findById(any())).thenReturn(Optional.empty());
        boolean deleted = service.delete(UUID.randomUUID());
        assertThat(deleted).isFalse();
        verify(repo, never()).delete(any());
    }
}
