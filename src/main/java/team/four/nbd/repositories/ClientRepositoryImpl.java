package team.four.nbd.repositories;

import jakarta.persistence.EntityManager;
import team.four.nbd.data.Client;

public class ClientRepositoryImpl implements ClientRepository {
    private final EntityManager em;

    public ClientRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Client getClient(long id) {
        return em.find(Client.class, id);
    }

}
