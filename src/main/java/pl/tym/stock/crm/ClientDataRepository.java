package pl.tym.stock.crm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientDataRepository extends
        JpaRepository<ClientData, Integer> {
}
