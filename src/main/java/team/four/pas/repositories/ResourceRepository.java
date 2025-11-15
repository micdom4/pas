package team.four.pas.repositories;

import team.four.pas.data.resources.Resource;

import java.util.UUID;


interface ResourceRepository extends Repository<Resource> {
    boolean addVM(int cpuNumber, int ram, int memory);
    public boolean updateVM(UUID id, int cpuNumber, int ram, int memory);
}

