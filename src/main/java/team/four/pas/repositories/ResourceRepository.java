package team.four.pas.repositories;

import team.four.pas.data.resources.Resource;


interface ResourceRepository extends Repository<Resource> {
    boolean addVM(int cpuNumber, int ram, int memory);
}

