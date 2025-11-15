package team.four.pas.repositories;

import jakarta.persistence.EntityManager;
import team.four.pas.data.Client;

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
